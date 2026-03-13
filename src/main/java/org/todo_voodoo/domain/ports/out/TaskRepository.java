package org.todo_voodoo.domain.ports.out;

import org.todo_voodoo.domain.model.Task;

/**
 * This interface defines the interface for data storage. It represents the data requirements the
 * domain needs to fulfill actions such as saving, finding by id, or deleting.
 */
public interface TaskRepository
{
    Task save(Task task);
}
