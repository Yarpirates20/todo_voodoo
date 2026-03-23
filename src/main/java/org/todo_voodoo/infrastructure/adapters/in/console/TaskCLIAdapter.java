package org.todo_voodoo.infrastructure.adapters.in.console;

import org.todo_voodoo.domain.model.Task;
import org.todo_voodoo.domain.ports.in.TaskUseCase;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * A basic command-line interface which is an incoming driver of the domain and
 * handles I/O.
 */
public class TaskCLIAdapter
{
    private final TaskUseCase taskService;
    private final Scanner scanner;


    /**
     * Constructor.
     *
     * @param taskService
     */
    public TaskCLIAdapter(TaskUseCase taskService)
    {
        this.taskService = taskService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * This method starts the application's interactive CLI loop
     * and keeps it alive until the user exists.
     * <p>
     * The run() method prints the menu, reads the user's choice, and calls
     * appropriate helper functions.
     */
    public void run()
    {
        boolean running = true;
        while (running)
        {
            printMenu();
            int command = Integer.parseInt(scanner.nextLine());

            if (command == 0)
                running = false;
            else
            {
                handleCommand(command);
            }
        }

    }

    /**
     * Calls the appropriate helper function for menu options.
     *
     * @param command Parsed int reflecting user's menu input choice.
     */
    private void handleCommand(int command)
    {
        switch (command)
        {
            case 1:
                handleGetAllTasks();
                break;
            case 2:
                handleCreateTask();
                break;
//            case 3:
//                handleCompleteTask();
//                break;
            case 4:
                handleRenameTask();
                break;
//            case 5:
//                handlePostponeTask();
//                break;
//            case 6:
//                handle deleteTask();
//                break;
            default:
                System.out.println("Invalid command");
                break;
        }
    }

    /**
     * Get task title and info from user and pass on to
     * task service.
     */
    private void handleCreateTask()
    {
        System.out.println("Enter the task title: ");
        String titleInput = scanner.nextLine();

        taskService.createTask(titleInput);
        System.out.println("New task successfully created.");
    }

    /**
     * Retrieves all available tasks.
     */
    private void handleGetAllTasks()
    {
        List<Task> allTasks = taskService.getAllTasks();

        if (allTasks.isEmpty())
        {
            System.out.println("There are no tasks available.");
        } else
        {
            allTasks.forEach(task -> System.out.println(task));
        }
    }

    /**
     * Gathers necessary data from user using Scanner and passes that on to
     * taskService.
     */
    private void handleRenameTask()
    {
        System.out.println("Enter the UUID of the task to rename: ");
        String idInput = scanner.nextLine();
        UUID id = UUID.fromString(idInput);

        System.out.print("Enter the new title: ");
        String newTitle = scanner.nextLine();

        // Call service to perform logic
        taskService.renameTask(id, newTitle);
        System.out.println("Task renamed successfully!");
    }

    /**
     * Displays the menu for user input.
     */
    private void printMenu()
    {
        System.out.println("--- TODO VOODOO MENU ---");

        System.out.println("1. View All Tasks");
        System.out.println("2. Create Task");
        System.out.println("3. Mark Task Complete");
        System.out.println("4. Rename Task");
        System.out.println("5. Postpone Task");
        System.out.println("6. Delete Task");
        System.out.println("0. Exit");

        System.out.print("Enter the menu option: ");


    }

}
