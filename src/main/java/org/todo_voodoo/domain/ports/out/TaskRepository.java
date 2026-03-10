package org.todo_voodoo.domain.ports.out;

import org.todo_voodoo.domain.model.Task;

public interface TaskRepository
{
    void save(Task task);
}
