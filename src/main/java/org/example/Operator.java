package org.example;

public class Operator extends User{
    private String department;

    Operator() {}

    Operator(String name, String role, String email, String dept) {
        super(name, role, email);
        this.department = dept;
    }

    public String getDepartment() {
        return department;
    }


}
