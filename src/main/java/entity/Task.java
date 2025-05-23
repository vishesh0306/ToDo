package entity;

import java.time.LocalDate;

public class Task {
    private int id;
    private String title;
    private String description;
    private Priority priority;
    private LocalDate dueDate;
    private Status status;
    private String createdBy;

    private Task(int id, String title, String description, Priority priority,
                 LocalDate dueDate, Status status, String createdBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.status = status;
        this.createdBy = createdBy;
    }

    // Factory method
    public static Task createTask(int id, String title, String description, Priority priority,
                                  LocalDate dueDate, String createdBy) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Task title is required.");
        }
        if (priority == null || dueDate == null || createdBy == null) {
            throw new IllegalArgumentException("Priority, due date, and creator are required.");
        }

        return new Task(id, title.trim(), description != null ? description.trim() : "",
                priority, dueDate, Status.INCOMPLETE, createdBy);
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    // Toggle completion status
    public void toggleStatus() {
        this.status = (this.status == Status.COMPLETE) ? Status.INCOMPLETE : Status.COMPLETE;
    }
}
