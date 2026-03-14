package integration;

import org.junit.jupiter.api.Test;
import org.todo_voodoo.domain.model.Task;
import org.todo_voodoo.domain.ports.in.TaskUseCase;
import org.todo_voodoo.domain.ports.out.TaskRepository;
import org.todo_voodoo.domain.service.TaskService;
import org.todo_voodoo.infrastructure.adapters.out.persistance.InMemoryTaskRepository;

import static org.junit.jupiter.api.Assertions.*;

class MainIntegrationTest {
  @Test
  void mainFlowCreateRenameGetAndList() {
    TaskRepository taskRepo = new InMemoryTaskRepository();
    TaskUseCase taskService = new TaskService(taskRepo);

    Task createdTask = taskService.createTask("Start my Stats homework");
    assertNotNull(createdTask.getId());
    assertEquals("Start my Stats homework", createdTask.getTitle());

    taskService.renameTask(createdTask.getId(), "Rename my Stats homework");

    Task updatedTask = taskService.getTaskById(createdTask.getId());
    assertEquals("Rename my Stats homework", updatedTask.getTitle());

    assertEquals(1, taskService.getAllTasks().size());
  }
}
