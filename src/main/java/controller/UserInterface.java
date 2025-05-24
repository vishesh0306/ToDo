 package controller;

import entity.*;
import ServicePackage.TodoManager;
import Storage.FileHandler;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private final TodoManager manager;
    private final Scanner scanner;
    private final String filePath = "tasks.csv";

    public UserInterface() {
        this.manager = new ServicePackage.TodoManager();
        this.scanner = new Scanner(System.in);

        // Load saved tasks
        List<Task> loadedTasks = FileHandler.loadTasksFromFile(filePath);
        manager.loadExistingTasks(loadedTasks);
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1 :{
                	createTask();
                	break;
                }
                case 2 :{
                	updateTask();
                	break;
                }
                case 3 :{
                	deleteTask();
                	break;
                }
                case 4 :{
                	markComplete();
                	break;
                }
                case 5 :{
                	listTasks();
                	break;
                }
                case 6 :{
                	searchTask();
                	break;
                }
                case 7 :{
                	saveTasks();
                	break;
                }
                case 0 :{
                	running = false;
                	break;
                }
                default :{
                	System.out.println("Invalid choice.");
                	break;
                }
            }
        }

        System.out.println("Exiting Todo App. Goodbye!");
    }

    private void printMenu() {
        System.out.println("\n==== Todo Menu ====");
        System.out.println("1. Create Task");
        System.out.println("2. Edit Task");
        System.out.println("3. Delete Task");
        System.out.println("4. Mark Task Complete");
        System.out.println("5. List All Tasks");
        System.out.println("6. Search Tasks");
        System.out.println("7. Save File");
        System.out.println("0. Exit");
        System.out.println("====================");
    }

    private void createTask() {
        String title = getStringInput("Title: ");
        String description = getStringInput("Description: ");
        Priority priority = getPriorityInput();
        LocalDate dueDate = getDateInput("Due Date (yyyy-mm-dd): ");
        String user = getStringInput("Created by: ");

        Task task = manager.addTask(title, description, priority, dueDate, user);
        System.out.println("Task created: " + task.getId());
    }

    private void updateTask() {
        int id = getIntInput("Enter task ID to update: ");
        String title = getStringInput("New Title: ");
        String description = getStringInput("New Description: ");
        Priority priority = getPriorityInput();
        LocalDate dueDate = getDateInput("New Due Date (yyyy-mm-dd): ");

        boolean success = manager.updateTask(id, title, description, priority, dueDate);
        System.out.println(success ? "Updated." : "Task not found.");
    }

    private void deleteTask() {
        int id = getIntInput("Enter task ID to delete: ");
        String confirm = getStringInput("Are you sure? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes")) {
            boolean success = manager.deleteTask(id);
            System.out.println(success ? "Deleted." : "Task not found.");
        }
    }

    private void markComplete() {
        int id = getIntInput("Enter task ID to mark as complete: ");
        boolean success = manager.markTaskComplete(id);
        System.out.println(success ? "Marked complete." : "Task not found.");
    }

    private void listTasks() {
        List<Task> tasks = manager.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            System.out.println("\n--- Tasks ---");
            for (Task task : tasks) {
                System.out.printf("ID: %d | %s | %s | %s | %s | %s%n",
                        task.getId(), task.getTitle(), task.getPriority(),
                        task.getDueDate(), task.getStatus(), task.getCreatedBy());
            }
        }
    }

    private void searchTask() {
        String keyword = getStringInput("Enter keyword to search: ");
        List<Task> found = manager.searchByKeyword(keyword);
        if (found.isEmpty()) {
            System.out.println("No matches found.");
        } else {
            for (Task task : found) {
                System.out.printf("ID: %d | %s | %s | %s | %s | %s%n",
                        task.getId(), task.getTitle(), task.getPriority(),
                        task.getDueDate(), task.getStatus(), task.getCreatedBy());
            }
        }
    }

    private void saveTasks() {
        FileHandler.saveTasksToFile(manager.getAllTasks(), filePath);
        System.out.println("Tasks saved.");
    }

    // --- Input Helpers ---

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
            }
        }
    }

    private LocalDate getDateInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return LocalDate.parse(scanner.nextLine());
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Use yyyy-mm-dd.");
            }
        }
    }

    private Priority getPriorityInput() {
        while (true) {
            System.out.print("Priority (LOW, MEDIUM, HIGH): ");
            try {
                return Priority.valueOf(scanner.nextLine().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid priority.");
            }
        }
    }
}
