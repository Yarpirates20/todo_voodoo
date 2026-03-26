package org.todo_voodoo;


import org.todo_voodoo.domain.model.Task;
import org.todo_voodoo.domain.ports.in.TaskUseCase;
import org.todo_voodoo.domain.ports.out.TaskRepository;
import org.todo_voodoo.domain.service.TaskService;
import org.todo_voodoo.infrastructure.adapters.in.console.TaskCLIAdapter;
import org.todo_voodoo.infrastructure.adapters.out.persistance.InMemoryTaskRepository;

public class Main
{
    public static void main(String[] args)
    {
        // Create repository + service
        TaskRepository taskRepo = new InMemoryTaskRepository();
        TaskUseCase taskService = new TaskService(taskRepo);


        // Demo
        System.out.println("--- Starting To-Do Voodoo Test ---");

        // Use the Service
        Task testTask = taskService.createTask("Start my Stats homework");
        System.out.println("Created Task: " + testTask.getTitle() + " with ID: " + testTask.getId());




        // Rename Task
        taskService.renameTask(testTask.getId(), "Rename my Stats homework");

        // Retrieve and verify
        Task updatedTask = taskService.getTaskById(testTask.getId());
        System.out.println("Updated title: " + updatedTask.getTitle());

        // List All
        System.out.println("Total Tasks in System: " + taskService.getAllTasks().size());


        TaskCLIAdapter cli = new TaskCLIAdapter(taskService);
        cli.run();

    }
}
