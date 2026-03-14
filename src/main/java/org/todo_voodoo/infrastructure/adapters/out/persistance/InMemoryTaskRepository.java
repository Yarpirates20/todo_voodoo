package org.todo_voodoo.infrastructure.adapters.out.persistance;

import org.todo_voodoo.domain.model.Task;
import org.todo_voodoo.domain.ports.out.TaskRepository;

import java.util.*;

/**
 * This class is a simple HashMap in RAM to store tasks for testing.
 */
public class InMemoryTaskRepository implements TaskRepository
{
    private final Map<UUID, Task> db = new HashMap<>();

    @Override
    public Task save(Task task)
    {
        db.put(task.getId(), task);
        return task;
    }

    @Override
    public List<Task> findAll()
    {
        return new ArrayList<>(db.values());
    }

    @Override
    public void deleteById(UUID id)
    {
        db.remove(id);
    }

    @Override
    public Optional<Task> findById(UUID id)
    {
        return Optional.ofNullable(db.get(id));
    }
}
