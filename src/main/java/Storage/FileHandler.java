package Storage;

import entity.*;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

public class FileHandler {

    private static final String DELIMITER = ",";

    // Save list of tasks to file
    public static void saveTasksToFile(List<Task> tasks, String filePath) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            for (Task task : tasks) {
                String line = String.join(DELIMITER,
                        String.valueOf(task.getId()),
                        escape(task.getTitle()),
                        escape(task.getDescription()),
                        task.getPriority().name(),
                        task.getDueDate().toString(),
                        task.getStatus().name(),
                        escape(task.getCreatedBy())
                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    // Load tasks from file
    public static List<Task> loadTasksFromFile(String filePath) {
        List<Task> tasks = new ArrayList<>();
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            System.out.println("No existing task file found. Starting fresh.");
            return tasks;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = parseTaskFromCSV(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }

        return tasks;
    }

    // Convert CSV line into Task object
    private static Task parseTaskFromCSV(String line) {
        try {
            String[] parts = split(line);

            int id = Integer.parseInt(parts[0]);
            String title = unescape(parts[1]);
            String description = unescape(parts[2]);
            Priority priority = Priority.valueOf(parts[3]);
            LocalDate dueDate = LocalDate.parse(parts[4]);
            Status status = Status.valueOf(parts[5]);
            String createdBy = unescape(parts[6]);

            Task task = Task.createTask(id, title, description, priority, dueDate, createdBy);
            task.setStatus(status);
            return task;

        } catch (Exception e) {
            System.err.println("Skipping malformed task line: " + e.getMessage());
            return null;
        }
    }

    // Escape fields to handle commas
    private static String escape(String input) {
        return input.replace(",", "<comma>");
    }

    private static String unescape(String input) {
        return input.replace("<comma>", ",");
    }

    private static String[] split(String line) {
        return line.split(DELIMITER, -1);
    }
}
