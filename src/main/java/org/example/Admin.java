package org.example;

public class Admin extends Operator{
    private int clearanceLevel;

    Admin(String name, String role, String email, String dept, int cl) {
        super(name, role, email, dept);
        this.clearanceLevel = cl;
    }

    public int getClearanceLevel() {
        return this.clearanceLevel;
    }
}
