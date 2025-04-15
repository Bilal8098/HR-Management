package com.example.hrmanagementmanager;

public class Employee {
    private int employeeID;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private double salary;

    public Employee(int employeeID, String fullName, String email, String phone, String address, double salary) {
        this.employeeID = employeeID;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.salary = salary;
    }

    // Getters
    public int getEmployeeID() { return employeeID; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public double getSalary() { return salary; }
}
