package model;

/**
 * User entity representing a wallet holder.
 * One-to-One relationship with Wallet (one user has one wallet).
 */
public class User {

    private int id;
    private String username;
    private String email;
    private String name;

    public User(int id, String username, String email, String name) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
    }

    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getName() { return name; }

    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', email='" + email + "', name='" + name + "'}";
    }
}
