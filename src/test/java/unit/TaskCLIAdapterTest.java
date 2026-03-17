package unit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.todo_voodoo.domain.model.Task;
import org.todo_voodoo.domain.ports.in.TaskUseCase;
import org.todo_voodoo.infrastructure.adapters.in.console.TaskCLIAdapter;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskCLIAdapterTest
{
    private FakeTaskUseCase fakeUseCase;
    private TaskCLIAdapter adapter;
    private java.io.InputStream originalIn;

    @BeforeEach
    void setUp()
    {
        fakeUseCase = new FakeTaskUseCase();
        originalIn = System.in;
    }

    @AfterEach
    void tearDown()
    {
        System.setIn(originalIn);
    }

    @Test
    void run_renameCommand_callsRenameWithExpectedValues()
    {
        Task seeded = fakeUseCase.seedTask("Old title");
        String input = String.join(System.lineSeparator(), "4", seeded.getId().toString(), "New " +
                "title", "0") + System.lineSeparator();

        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        adapter = new TaskCLIAdapter(fakeUseCase);

        adapter.run();

        assertEquals(1, fakeUseCase.getRenameTaskCalls());
        assertEquals(seeded.getId(), fakeUseCase.getLastRenameId());
        assertEquals("New title", fakeUseCase.getLastRenameTitle());
        assertEquals("New title", fakeUseCase.getTaskById(seeded.getId()).getTitle());
    }

    static class FakeTaskUseCase implements TaskUseCase
    {
        private final Map<UUID, Task> storage = new HashMap<>();

        private int renameTaskCalls = 0;
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

        @Override
        public List<Task> getAllTasks()
        {
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
