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

    @Override
    public void deleteTask(UUID id)
    {

    }

    @Override
    public Task updateDescription(UUID id, String newDescription)
    {
        return null;
    }

    @Override
    public Task postponeTask(UUID id, String newDate)
    {
        return null;
    }

    @Override
    public Task reclassifyTask(UUID id, int categoryId)
    {
        return null;
    }

    /**
     * Uses the repository to find task and changes it using domain model's logic before saving
     * it back to repository.
     *
     * @param id UUID of task to update.
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

    @Override
    public List<Task> getAllTasks()
    {
        return List.of();
    }

    /**
     * Pulls task from repository into memory.
     *
     * @param id UUID of task to fetch.
     * @return
     */
    @Override
    public Task getTaskById(UUID id)
    {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("No task found with id: " + id));

    }

    @Override
    public Task completeTask(UUID id)
    {
        return null;
    }

    @Override
    public Task uncompleteTask(UUID id)
    {
        return null;
    }
}
