package org.todo_voodoo.domain.service;

import org.todo_voodoo.domain.model.Task;
import org.todo_voodoo.domain.ports.in.TaskUseCase;
import org.todo_voodoo.domain.ports.out.TaskRepository;

public class TaskService implements TaskUseCase
{
    private final TaskRepository repository;

    /**
     * Constructor
     * @param repository Repository to save data to.
     */
    public TaskService(TaskRepository repository)
    {
        this.repository = repository;
    }

    public void create(Task task)
    {
        // Business logic (e.g., validation, goes here)
        repository.save(task);
    }
}
