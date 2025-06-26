package model;

import java.util.Date;
import java.util.UUID;

public class User {

   private int id;
    private String username;
    private String password;
    private String fullName;
    private String role;
    private boolean enabled;
    private UUID resetToken;
    private Date tokenExpiry;
    private String email;
    public User() {
    }

    public User(int id, String username, String password, String fullName, String role, boolean enabled, UUID resetToken, Date tokenExpiry, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.enabled = enabled;
        this.resetToken = resetToken;
        this.tokenExpiry = tokenExpiry;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public UUID getResetToken() {
        return resetToken;
    }

    public void setResetToken(UUID resetToken) {
        this.resetToken = resetToken;
    }

    public Date getTokenExpiry() {
        return tokenExpiry;
    }

    public void setTokenExpiry(Date tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
        @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', fullName='" + fullName + "', role='" + role + "', enabled=" + enabled + ", email='" + email + "'}";
    }
    
}
