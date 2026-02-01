package org.example;

public class UserFactory {

    public User build(String name, String role, String email, String department, Integer clearanceLevel) {
        if (role.equals("Admin")) {
            return new Admin(name, role, email, department, clearanceLevel);
        } else if (role.equals("Operator")) {
            return new Operator(name, role, email, department);
        } else {
            return new User(name, role, email);
        }
    }
}
