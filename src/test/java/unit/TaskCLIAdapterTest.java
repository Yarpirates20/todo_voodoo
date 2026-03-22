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
        assertEquals(1,fakeUseCase.getGetAllTasksCalls());
    }

    @Test
    void runViewAllTasksPrintsEmptyMessageWhenNoTasks()
    {
        String input = String.join(System.lineSeparator(),"1", "0") + System.lineSeparator();
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        System.setOut(new PrintStream(out));

        adapter = new TaskCLIAdapter(fakeUseCase);
        adapter.run();

        String output = out.toString(StandardCharsets.UTF_8);

        assertTrue(output.contains("There are no tasks available."));
        assertEquals(1, fakeUseCase.getGetAllTasksCalls());
    }

    static class FakeTaskUseCase implements TaskUseCase
    {
        private final Map<UUID, Task> storage = new HashMap<>();

        private int renameTaskCalls = 0;
        private int getAllTasksCalls = 0;

        private UUID lastRenameId;
        private String lastRenameTitle;

        Task seedTask(String title)
        {
            Task task = new Task(title);
            storage.put(task.getId(), task);
            return task;
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
            Task task = new Task(title);
            storage.put(task.getId(), task);
            return task;
        }

        @Override
        public void deleteTask(UUID id)
        {
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
            throw new UnsupportedOperationException("Not needed in this test");
        }

        @Override
        public Task reclassifyTask(UUID id, int categoryId)
        {
            throw new UnsupportedOperationException("Not needed in this test");
        }


        @Override
        public Task completeTask(UUID id)
        {
            throw new UnsupportedOperationException("Not needed in this test");
        }

        @Override
        public Task uncompleteTask(UUID id)
        {
            throw new UnsupportedOperationException("Not needed in this test");
        }
    }
}
