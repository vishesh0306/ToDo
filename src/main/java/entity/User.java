package entity;

public class User {
    private String username;

    // Constructor is private to encourage use of factory
    private User(String username) {
        this.username = username;
    }

    // Factory method
    public static User createUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        return new User(username.trim());
    }

    // Getter & Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        this.username = username.trim();
    }
}