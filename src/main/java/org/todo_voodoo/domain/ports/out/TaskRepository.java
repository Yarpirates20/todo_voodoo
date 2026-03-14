package org.todo_voodoo.domain.ports.out;

import org.todo_voodoo.domain.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This interface defines the interface for data storage. It represents the data requirements the
 * domain needs to fulfill actions such as saving, finding by id, or deleting.
 */
public interface TaskRepository
{
    /**
     * Persists the given Task to storage system.
     *
     * If task already exists (matching ID), it will be updated.
     * If task is new, it will be inserted.
     *
     * @param task The Task entity to be saved.
     * @return The persisted Task, possibly with updated storage-level metadata.
     */
    Task save(Task task);

    /**
     *  Retrieves all data for list view.
     *
     * @return A collection of all Tasks in data storage as a List.
     */
    List<Task> findAll();

    /**
     * Removes a specific task from storage.
     *
     * @param id UUID of the tassk to delete.
     */
    void deleteById(UUID id);

    /**
     * Fetches a specific task.
     *
     * @param id UUID of task to fetch.
     * @return An Optional containing the Task if found, or an empty Optional if no task exists with that ID.
     */
    Optional<Task> findById(UUID id);
}
