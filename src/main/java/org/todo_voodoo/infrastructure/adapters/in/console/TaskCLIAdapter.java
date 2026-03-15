package org.todo_voodoo.infrastructure.adapters.in.console;

import org.todo_voodoo.domain.ports.in.TaskUseCase;

import java.util.Scanner;

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
     *
     * The run() method prints the menu, reads the user's choice, and calls
     * appropriate helper functions.
     */
    public void run()
    {
        boolean running = true;
        while (running)
        {
            printMenu();
            String command = scanner.nextLine();

            if (command.equals("exit"))
                running = false;
            else
            {
                handleCommand(command);
            }
        }

    }

    /**
     *
     */
    public void printMenu()
    {
    }

}
