package org.todo_voodoo;


import org.todo_voodoo.domain.service.TaskService;
import org.todo_voodoo.infrastructure.adapters.in.console.TaskCLIAdapter;
import org.todo_voodoo.infrastructure.adapters.out.persistance.PostgresTaskRepository;

public class Main
{
    public static void main(String[] args)
    {
        // Initialize storage (Outbound Adaptor)
        var repository = new PostgresTaskRepository();

        // Initialize logic (Domain Service)
        var taskService = new TaskService(repository);

        // Initialize CLI (Inbound Adaptor)


    }
}
