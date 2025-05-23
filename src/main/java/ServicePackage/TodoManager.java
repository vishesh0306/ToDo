package ServicePackage;

import entity.Task;
import entity.Status;
import entity.Priority;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TodoManager {
    private final List<Task> tasks;
    private int nextId;

    // Constructor
    public TodoManager() {
        this.tasks = new ArrayList<>();
        this.nextId = 1;
    }

    // ====== Core Methods ======

    public Task addTask(String title, String description, Priority priority, LocalDate dueDate, String createdBy) {
        Task task = Task.createTask(nextId++, title, description, priority, dueDate, createdBy);
        tasks.add(task);
        return task;
    }

    public boolean updateTask(int taskId, String title, String description, Priority priority, LocalDate dueDate) {
        Task task = getTaskById(taskId);
        if (task == null) return false;

        if (title != null && !title.trim().isEmpty()) task.setTitle(title);
        if (description != null) task.setDescription(description);
        if (priority != null) task.setPriority(priority);
        if (dueDate != null) task.setDueDate(dueDate);
        return true;
    }

    public boolean deleteTask(int taskId) {
        return tasks.removeIf(task -> task.getId() == taskId);
    }

    public boolean markTaskComplete(int taskId) {
        Task task = getTaskById(taskId);
        if (task == null) return false;
        task.setStatus(Status.COMPLETE);
        return true;
    }

    public boolean markTaskIncomplete(int taskId) {
        Task task = getTaskById(taskId);
        if (task == null) return false;
        task.setStatus(Status.INCOMPLETE);
        return true;
    }

    public Task getTaskById(int taskId) {
        return tasks.stream()
                .filter(task -> task.getId() == taskId)
                .findFirst()
                .orElse(null);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks); // Return a copy to preserve encapsulation
    }

    // ====== Search & Filter ======

    public List<Task> searchByKeyword(String keyword) {
        String lower = keyword.toLowerCase();
        return tasks.stream()
                .filter(task ->
                        task.getTitle().toLowerCase().contains(lower) ||
                        task.getDescription().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public List<Task> filterByStatus(Status status) {
        return tasks.stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Task> filterByPriority(Priority priority) {
        return tasks.stream()
                .filter(task -> task.getPriority() == priority)
                .collect(Collectors.toList());
    }

    public List<Task> filterByDueDateRange(LocalDate from, LocalDate to) {
        return tasks.stream()
                .filter(task -> !task.getDueDate().isBefore(from) && !task.getDueDate().isAfter(to))
                .collect(Collectors.toList());
    }

    // ====== Sorting ======

    public List<Task> sortByDueDate() {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getDueDate))
                .collect(Collectors.toList());
    }

    public List<Task> sortByPriority() {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getPriority))
                .collect(Collectors.toList());
    }

    public List<Task> sortByCreationId() {
        return tasks.stream()
                .sorted(Comparator.comparingInt(Task::getId))
                .collect(Collectors.toList());
    }

    // ====== Utilities ======

    public void clearAllTasks() {
        tasks.clear();
        nextId = 1;
    }

    public void loadExistingTasks(List<Task> loadedTasks) {
        tasks.clear();
        tasks.addAll(loadedTasks);
        this.nextId = tasks.stream()
                .mapToInt(Task::getId)
                .max()
                .orElse(0) + 1;
    }
}