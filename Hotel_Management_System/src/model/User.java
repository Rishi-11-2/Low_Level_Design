package model;

public class User {
    private final String id;
    private final String name;
    private final String email;
    private final UserRole role;
    private final long createdAt;

    public User(String id, String name, String email, UserRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdAt = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
