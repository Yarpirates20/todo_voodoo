package unit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.todo_voodoo.domain.model.Task;
import org.todo_voodoo.domain.ports.in.TaskUseCase;
import org.todo_voodoo.infrastructure.adapters.in.console.TaskCLIAdapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskCLIAdapterTest
{
    private FakeTaskUseCase fakeUseCase;
    private TaskCLIAdapter adapter;
    private java.io.InputStream originalIn;
    private java.io.PrintStream originalOut;


    @BeforeEach
    void setUp()
    {
        fakeUseCase = new FakeTaskUseCase();
        originalIn = System.in;
        originalOut = System.out;
    }

    @AfterEach
    void tearDown()
    {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    void runRenameCommandCallsRenameWithExpectedValues()
    {
        Task seeded = fakeUseCase.seedTask("Old title");
        String input = String.join(System.lineSeparator(),
                "4",
                seeded.getId().toString(),
                "New " +
                        "title",
                "0") + System.lineSeparator();

        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        adapter = new TaskCLIAdapter(fakeUseCase);

        adapter.run();

        assertEquals(1, fakeUseCase.getRenameTaskCalls());
        assertEquals(seeded.getId(), fakeUseCase.getLastRenameId());
        assertEquals("New title", fakeUseCase.getLastRenameTitle());
        assertEquals("New title", fakeUseCase.getTaskById(seeded.getId()).getTitle());
    }

    @Test
    void runViewAllTasksPrintsTasksWhenTaskExists()
    {
        Task t1 = fakeUseCase.seedTask("First task");
        Task t2 = fakeUseCase.seedTask("Second task");

        String input = String.join(System.lineSeparator(), "1", "0") + System.lineSeparator();
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        adapter = new TaskCLIAdapter(fakeUseCase);
        adapter.run();

        String output = out.toString(StandardCharsets.UTF_8);

        assertTrue(output.contains(t1.getTitle()));
        assertTrue(output.contains(t2.getTitle()));
        assertEquals(1, fakeUseCase.getGetAllTasksCalls());
    }

    @Test
    void runViewAllTasksPrintsEmptyMessageWhenNoTasks()
    {
        String input = String.join(System.lineSeparator(), "1", "0") + System.lineSeparator();
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        System.setOut(new PrintStream(out));

        adapter = new TaskCLIAdapter(fakeUseCase);
        adapter.run();

        String output = out.toString(StandardCharsets.UTF_8);

        assertTrue(output.contains("There are no tasks available."));
        assertEquals(1, fakeUseCase.getGetAllTasksCalls());
    }

    @Test
    void runCreateTaskCallsCreateTaskAndPrintsSuccessMessage()
    {
        String input =
                String.join(System.lineSeparator(), "2", "Buy milk", "0") + System.lineSeparator();

        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        adapter = new TaskCLIAdapter(fakeUseCase);
        adapter.run();

        String output = out.toString(StandardCharsets.UTF_8);

        assertEquals(1, fakeUseCase.getCreateTaskCalls());
        assertEquals("Buy milk", fakeUseCase.getLastCreatedTitle());
        assertEquals(1, fakeUseCase.getTaskCount());
        assertTrue(fakeUseCase.containsTaskWithTitle("Buy milk"));
        assertTrue(output.contains("New task successfully created."));
    }

    @Test
    void runCompleteTaskCallsCompleteTaskAndPrintSuccessMessage()
    {
        Task seeded = fakeUseCase.seedTask("Pay bills");

        String input =
                String.join(System.lineSeparator(), "3", seeded.getId().toString(), "0") + System.lineSeparator();

        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        adapter = new TaskCLIAdapter(fakeUseCase);
        adapter.run();

        String output = out.toString(StandardCharsets.UTF_8);

        assertEquals(1, fakeUseCase.getCompleteTaskCalls());
        assertEquals(seeded.getId(), fakeUseCase.getLastCompletedTaskId());
        assertTrue(fakeUseCase.getTaskById(seeded.getId()).getIsCompleted());
        assertTrue(output.contains("Task marked completed"));
    }

    @Test
    void runPostponeTaskCallsPostponeTaskWithExpectedValues()
    {
        Task seeded = fakeUseCase.seedTask("Submit report");

        String input = String.join(System.lineSeparator(),
                "5",
                seeded.getId().toString(),
                "12",
                "31",
                "2026",
                "0") + System.lineSeparator();

        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        adapter = new TaskCLIAdapter(fakeUseCase);
        adapter.run();

        assertEquals(1, fakeUseCase.getPostponeTaskCalls());
        assertEquals(seeded.getId(), fakeUseCase.getLastPostponedTaskId());
        assertEquals("2026-12-31", fakeUseCase.getLastPostponedDate());
        assertEquals("2026-12-31", fakeUseCase.getTaskById(seeded.getId()).getDueDate().toString());
    }

    @Test
    void runDeleteTaskCallsDeleteTaskAndPrintsSuccessMessage()
    {
        Task seeded = fakeUseCase.seedTask("Clean kitchen");

        String input = String.join(System.lineSeparator(),
                "6",
                seeded.getId().toString(),
                "0") + System.lineSeparator();

        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        adapter = new TaskCLIAdapter(fakeUseCase);
        adapter.run();

        String output = out.toString(StandardCharsets.UTF_8);

        assertEquals(1, fakeUseCase.getDeleteTaskCalls());
        assertEquals(seeded.getId(), fakeUseCase.getLastDeletedTaskId());
        assertEquals(0, fakeUseCase.getTaskCount());
        assertTrue(output.contains("Task deleted successfully."));
    }

    static class FakeTaskUseCase implements TaskUseCase
    {
        private final Map<UUID, Task> storage = new HashMap<>();

        private int renameTaskCalls = 0;
        private int getAllTasksCalls = 0;

        private UUID lastRenameId;
        private String lastRenameTitle;

        private int createTaskCalls = 0;
        private String lastCreatedTitle;

        private int completeTaskCalls = 0;
        private UUID lastCompletedTaskId;

        private int postponeTaskCalls = 0;
        private UUID lastPostponedTaskId;
        private String lastPostponedDate;

        private int deleteTaskCalls = 0;
        private UUID lastDeletedTaskId;

        public int getDeleteTaskCalls()
        {
            return deleteTaskCalls;
        }

        public UUID getLastDeletedTaskId()
        {
            return lastDeletedTaskId;
        }

        Task seedTask(String title)
        {
            Task task = new Task(title);
            storage.put(task.getId(), task);
            return task;
        }

        int getPostponeTaskCalls()
        {
            return postponeTaskCalls;
        }

        public UUID getLastPostponedTaskId()
        {
            return lastPostponedTaskId;
        }

        public String getLastPostponedDate()
        {
            return lastPostponedDate;
        }

        int getCompleteTaskCalls()
        {
            return completeTaskCalls;
        }

        UUID getLastCompletedTaskId()
        {
            return lastCompletedTaskId;
        }

        int getRenameTaskCalls()
        {
            return renameTaskCalls;
        }

        UUID getLastRenameId()
        {
            return lastRenameId;
        }

        String getLastRenameTitle()
        {
            return lastRenameTitle;
        }

        int getCreateTaskCalls()
        {
            return createTaskCalls;
        }

        int getTaskCount()
        {
            return storage.size();
        }

        boolean containsTaskWithTitle(String title)
        {
            return storage.values().stream().anyMatch(task -> task.getTitle().equals(title));
        }

        String getLastCreatedTitle()
        {
            return lastCreatedTitle;
        }

        @Override
        public Task renameTask(UUID id, String newTitle)
        {
            renameTaskCalls++;
            lastRenameId = id;
            lastRenameTitle = newTitle;

            Task task = storage.get(id);

            if (task == null)
            {
                throw new IllegalArgumentException("No task found with id: " + id);
            }

            task.rename(newTitle);

            return task;
        }

        @Override
        public Task getTaskById(UUID id)
        {
            Task task = storage.get(id);
            if (task == null)
            {
                throw new IllegalArgumentException("No task found with id: " + id);
            }

            return task;
        }

        int getGetAllTasksCalls()
        {
            return getAllTasksCalls;
        }

        @Override
        public List<Task> getAllTasks()
        {
            getAllTasksCalls++;
            return List.copyOf(storage.values());
        }

        @Override
        public Task createTask(String title)
        {
            createTaskCalls++;
            lastCreatedTitle = title;

            Task task = new Task(title);
            storage.put(task.getId(), task);
            return task;
        }

        @Override
        public void deleteTask(UUID id)
        {
            deleteTaskCalls++;
            lastDeletedTaskId = id;
            storage.remove(id);
        }

        @Override
        public Task updateDescription(UUID id, String newDescription)
        {
            throw new UnsupportedOperationException("Not needed in this test");
        }

        @Override
        public Task postponeTask(UUID id, String newDate)
        {
            postponeTaskCalls++;
            lastPostponedTaskId = id;
            lastPostponedDate = newDate;

            Task task = storage.get(id);

            if (task == null)
            {
                throw new IllegalArgumentException("No task found with id: " + id);
            }

            task.updateDueDate(newDate);
            return task;
        }

        @Override
        public Task reclassifyTask(UUID id, int categoryId)
        {
            throw new UnsupportedOperationException("Not needed in this test");
        }


        @Override
        public Task completeTask(UUID id)
        {
            completeTaskCalls++;
            lastCompletedTaskId = id;

            Task task = storage.get(id);

            if (task == null)
            {
                throw new IllegalArgumentException("No task found with id: " + id);
            }

            task.complete();
            return task;
        }

        @Override
        public Task uncompleteTask(UUID id)
        {
            throw new UnsupportedOperationException("Not needed in this test");
        }

    }
}
