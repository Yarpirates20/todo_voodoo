package org.todo_voodoo.domain.service;

import org.todo_voodoo.domain.model.Task;
import org.todo_voodoo.domain.ports.out.TaskRepository;
import org.todo_voodoo.domain.ports.in.TaskUseCase;

import java.util.List;
import java.util.UUID;

public class TaskService implements TaskUseCase
{
    private final TaskRepository repository;

    /**
     * Constructor
     *
     * @param repository Repository to save data to.
     */
    public TaskService(TaskRepository repository)
    {
        this.repository = repository;
    }

    /**
     * Takes user input (title) and transforms it into domain Task entity that persists in system.
     *
     * @param title The title of task as a string.
     * @return Fully populated Task entity to be sent back to caller.
     */
    @Override
    public Task createTask(String title)
    {
        Task t = new Task(title);

        Task savedTask = repository.save(t);

        return savedTask;
    }

    /**
     * Uses the TaskService getTaskById to ensure there is a task to delete, then deletes task
     * from repository.
     *
     * If no task with that UUID exists, throws exception.
     *
     * @param id UUID of task to delete.
     */
    @Override
    public void deleteTask(UUID id)
    {
         getTaskById(id);

         repository.deleteById(id);
    }

    /**
     * Updates existing task description.
     *
     * @param id UUID of task to update.
     * @param newDescription The new description string.
     * @return The task with updated description.
     */
    @Override
    public Task updateDescription(UUID id, String newDescription)
    {
        Task task = getTaskById(id);
        task.updateDescription(newDescription);
        return repository.save(task);
    }

    /**
     * Postpones a task by changing its due date.
     *
     * @param id      UUID of task to postpone.
     * @param newDate The new due date string.
     * @return Task with updated due date.
     */
    @Override
    public Task postponeTask(UUID id, String newDate)
    {
        Task task = getTaskById(id);
        task.updateDueDate(newDate);
        return repository.save(task);
    }

    /**
     * Uses repository to find task and change its category, then saves back to repository.
     *
     * @param id         UUID of task to reclassify.
     * @param categoryId The new category ID.
     * @return The task with updated category.
     */
    @Override
    public Task reclassifyTask(UUID id, int categoryId)
    {
        Task task = getTaskById(id);

        task.updateCategory(categoryId);

        return repository.save(task);

    }

    /**
     * Uses the repository to find task and changes it using domain model's logic before saving
     * it back to repository.
     *
     * @param id       UUID of task to update.
     * @param newTitle The new title string.
     * @return The renamed task.
     */
    @Override
    public Task renameTask(UUID id, String newTitle)
    {
        // Fetch existing task
        Task task = getTaskById(id);

        // Change state using Domain Model behavior.
        task.rename(newTitle);

        // Persist changes
        return repository.save(task);
    }


    /**
     * Acts as passthrough handling request from inbound port reaches data layer.
     * @return A List of all Tasks in data source.
     */
    @Override
    public List<Task> getAllTasks()
    {
        return repository.findAll();
    }

    /**
     * Pulls task from repository into memory.
     *
     * @param id UUID of task to fetch.
     * @return Task if exists or else throws exception.
     */
    @Override
    public Task getTaskById(UUID id)
    {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("No task found with id: " + id));

    }

    /**
     * Toggles task status to completed.
     *
     * @param id UUID of task to complete.
     * @return Updated task marked completed.
     */
    @Override
    public Task completeTask(UUID id)
    {
        Task task = getTaskById(id);

        task.complete();

        return repository.save(task);

    }

    /**
     * Toggles task uncompleted.
     *
     * @param id UUID of task to mark uncompleted.
     * @return Task marked as uncompleted.
     */
    @Override
    public Task uncompleteTask(UUID id)
    {

        Task task = getTaskById(id);

        task.reopen();

        return  repository.save(task);

    }
}
