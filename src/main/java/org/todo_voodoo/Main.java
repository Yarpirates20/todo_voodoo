package org.todo_voodoo;


import org.todo_voodoo.domain.ports.in.TaskUseCase;
import org.todo_voodoo.domain.ports.out.TaskRepository;
import org.todo_voodoo.domain.service.TaskService;
import org.todo_voodoo.infrastructure.adapters.out.persistance.InMemoryTaskRepository;

public class Main
{
    public static void main(String[] args)
    {
        // Create the tool (Adapter)
        TaskRepository taskRepo = new InMemoryTaskRepository();

        // Create brain (Service) and give it the Tool
        TaskUseCase taskService = new TaskService(taskRepo);

        // Use the Service
        taskService.createTask("Start my Stats homework");


    }
}
