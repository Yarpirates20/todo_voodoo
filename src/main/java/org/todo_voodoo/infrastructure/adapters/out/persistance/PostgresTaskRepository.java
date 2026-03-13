package org.todo_voodoo.infrastructure.adapters.out.persistance;

import org.todo_voodoo.domain.model.Task;
import org.todo_voodoo.domain.ports.out.TaskRepository;

public class PostgresTaskRepository implements TaskRepository
{
    @Override
    public Task save(Task task)
    {
        return null;
    }
}
