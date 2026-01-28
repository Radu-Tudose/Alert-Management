package org.example;

public class User {

    private String name;
    private String role;
    private String email;

    User() {}

    User(String name, String role, String email) {
        this.name = name;
        this.role = role;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }
}
