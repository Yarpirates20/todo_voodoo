package org.todo_voodoo.domain.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Task is a Domain Model representing a To-Do list item.
 */
public class Task
{
    private UUID id;
    private String title;
    private String description;
    private LocalDate dueDate;        // YYYY-MM-DD
    private int categoryId;
    private boolean isCompleted;

    /**
     * All fields constructor for Database Adapter.
     * <p>
     * Use when fields are known at time of object creation.
     *
     * @param id           The generated task ID.
     * @param title        A title for a task that should never be empty.
     * @param description  Detailed description of task which can be empty.
     * @param dueDate     Optional due date for task to be completed by.
     * @param categoryId  ID of category of task.
     * @param isCompleted Boolean determining if task is active or finished.
     */
    public Task(UUID id, String title, String description, LocalDate dueDate, int categoryId,
                boolean isCompleted)
    {
        if (title == null || title.isBlank())
        {
            throw new IllegalArgumentException("Task title cannot be empty.");
        }

        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.categoryId = categoryId;
        this.isCompleted = isCompleted;
    }

    /**
     * New Task constructor for User/Service
     *
     * @param title Desired Task title.
     */
    public Task(String title)
    {
        this(java.util.UUID.randomUUID(), title, null, null, 1, false);
    }

    /**
     * Get Title.
     *
     * @return Title as String.
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Get description of Task.
     *
     * @return Description as String.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Get Due Date.
     *
     * @return String representing Due Date.
     */
    public LocalDate getDueDate()
    {
        return dueDate;
    }

    /**
     * Get Category ID.
     *
     * @return Integer of Category.
     */
    public int getCategoryId()
    {
        return categoryId;
    }

    /**
     * Get status.
     *
     * @return True if task completed, else false.
     */
    public boolean getIsCompleted()
    {
        return isCompleted;
    }

    /**
     * Get task ID.
     *
     * @return Task ID.
     */
    public UUID getId()
    {
        return id;
    }

    /**
     * Mark Task as completed.
     */
    public void complete()
    {
        this.isCompleted = true;
    }

    /**
     * Mark Task as uncompleted.
     */
    public void reopen()
    {
        this.isCompleted = false;
    }

    /**
     * Renames Task.
     *
     * @param newTitle The new title String.
     */
    public void rename(String newTitle)
    {
        if (newTitle == null || newTitle.isBlank())
        {
            throw new IllegalArgumentException("Task title cannot be empty.");
        }

        this.title = newTitle;
    }

    /**
     * Update Task description.
     *
     * @param newDescription The new description String.
     */
    public void updateDescription(String newDescription)
    {
        this.description = newDescription;
    }

    /**
     * Update due date
     *
     * @param newDate The new due date.
     */
    public void updateDueDate(String newDate)
    {
        this.dueDate = LocalDate.parse(newDate);
    }

    /**
     * Update the category of Task.
     *
     * @param newCategoryId ID of the new category.
     */
    public void updateCategory(int newCategoryId)
    {
        this.categoryId = newCategoryId;
    }

    /**
     * Turns object into text.
     *
     * @return String representation of Task.
     */
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("Task{");
        sb.append("id='").append(id).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", due_date='").append(dueDate).append('\'');
        sb.append(", category_id=").append(categoryId);
        sb.append(", is_completed=").append(isCompleted);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Compares Task ID's to determine if they are the same.
     *
     * @param o   the reference object with which to compare.
     * @return    True if objects are equal, else false.
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    /**
     * Generates a hash code for the Task based strictly on its unique identifier.
     * This ensures that the Task remains findable in Hash-based collections
     * even if its title or status is modified.
     *
     * @return an integer hash code derived from the task ID.
     */
    @Override
    public int hashCode()
    {
        return Objects.hashCode(id);
    }
}
