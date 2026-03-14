package unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.todo_voodoo.domain.model.Task;
import org.todo_voodoo.domain.ports.out.TaskRepository;
import org.todo_voodoo.domain.service.TaskService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskServiceTest {
  private TaskService taskService;
  private FakeTaskRepository repository;

  @BeforeEach
  void setUp() {
    repository = new FakeTaskRepository();
    taskService = new TaskService(repository);
  }

  @Test
  void createTaskSavesAndReturnsTask() {
    Task created = taskService.createTask("Write unit tests");

    assertNotNull(created.getId());
    assertEquals("Write unit tests", created.getTitle());
    assertEquals(1, repository.saveCalls);
    assertTrue(repository.findById(created.getId()).isPresent());
  }

  @Test
  void renameTaskUpdatesTitleAndPersists() {
    Task created = taskService.createTask("Old title");

    Task renamed = taskService.renameTask(created.getId(), "New title");

    assertEquals("New title", renamed.getTitle());
    assertEquals("New title", repository.findById(created.getId()).orElseThrow().getTitle());
  }

  @Test
  void updateDescriptionUpdatesDescriptionAndPersists() {
    Task created = taskService.createTask("Task with description");

    Task updated = taskService.updateDescription(created.getId(), "Updated description");

    assertEquals("Updated description", updated.getDescription());
    assertEquals(
        "Updated description", repository.findById(created.getId()).orElseThrow().getDescription());
  }

  @Test
  void reclassifyTaskUpdatesCategoryAndPersists() {
    Task created = taskService.createTask("Task to categorize");

    Task reclassified = taskService.reclassifyTask(created.getId(), 42);

    assertEquals(42, reclassified.getCategoryId());
    assertEquals(42, repository.findById(created.getId()).orElseThrow().getCategoryId());
  }

  @Test
  void getAllTasksReturnsAllSavedTasks() {
    taskService.createTask("Task 1");
    taskService.createTask("Task 2");

    List<Task> allTasks = taskService.getAllTasks();

    assertEquals(2, allTasks.size());
  }

  @Test
  void getTaskByIdWhenMissingThrowsMeaningfulException() {
    UUID missingId = UUID.randomUUID();

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> taskService.getTaskById(missingId));

    assertTrue(exception.getMessage().contains("No task found with id: " + missingId));
  }

  private static class FakeTaskRepository implements TaskRepository {
    private final Map<UUID, Task> storage = new HashMap<>();
    private int saveCalls = 0;

    @Override
    public Task save(Task task) {
      saveCalls++;
      storage.put(task.getId(), task);
      return task;
    }

    @Override
    public List<Task> findAll() {
      return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(UUID id) {
      storage.remove(id);
    }

    @Override
    public Optional<Task> findById(UUID id) {
      return Optional.ofNullable(storage.get(id));
    }
  }
}
