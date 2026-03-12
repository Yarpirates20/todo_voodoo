package org.todo_voodoo.domain.ports.in;

import org.todo_voodoo.domain.model.Task;

import java.util.List;
import java.util.UUID;

/**
 * This interface represents the use cases that can be triggered from outside
 * the application. A UI or API uses this to create, view, update, or delete Tasks.
 */
public interface TaskUseCase
{
    /* ******** Mutation methods ******** */
    /**
     * Creates a new Task.
     *
     * Generates unique ID and defaults status to "not completed".
     *
     * @param title The title of task as a string.
     * @return The created Task entity.
     */
    Task createTask(String title);

    /**
     * Deletes a task.
     * @param id UUID of task to delete.
     */
    void deleteTask(UUID id);

    /**
     * Replaces a task description with a new provided description.
     *
     * @param id UUID of task to update.
     * @param newDescription The new description string.
     * @return The updated Task.
     */
    Task updateDescription(UUID id, String newDescription);

    /**
     * Changes the due date of a task.
     *
     * @param id      UUID of task to postpone.
     * @param newDate The new due date string.
     * @return The updated task.
     */
    Task postponeTask(UUID id, String newDate);

    /**
     * Changes task category field.
     *
     * @param id UUID of task to reclassify.
     * @param categoryId The new category ID.
     * @return The updated task.
     */
    Task reclassifyTask(UUID id, int categoryId);

    /**
     * Changes an existing task's title.
     * @param id UUID of task to update.
     * @param newTitle The new title string.
     * @return The updated task.
     */
    Task renameTask(UUID id, String newTitle);

    /* ********* Getter methods ********* */
    /**
     * Retrieves all Tasks.
     *
     * @return A Java List of tasks.
     */
    List<Task> getAllTasks();

    /**
     * Fetches a unique task.
     *
     * @param id UUID of task to fetch.
     * @return The Task entity with matching UUID.
     */
    Task getTaskById(UUID id);

    /**
     * Updates a tasks status.
     *
     * @param id UUID of task to complete.
     * @return Returns the updated Task entity.
     */
    Task completeTask(UUID id);

    /**
     * Toggles a completed task's status back to uncompleted.
     *
     * @param id UUID of task to mark uncompleted.
     * @return Updated task.
     */
    Task uncompleteTask(UUID id);

}
