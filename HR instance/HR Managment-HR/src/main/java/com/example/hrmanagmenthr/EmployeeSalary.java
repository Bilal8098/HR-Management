package com.example.hrmanagmenthr;

public class EmployeeSalary {
    private String mainSalary;
    private String actualSalary;
    private int employeeID;
    private String differenceAmount;
    private String type;
    private String reason;
    // Constructors, getters, and setters
    public EmployeeSalary(String mainSalary, String actualSalary) {
        this.mainSalary = mainSalary;
        this.actualSalary = actualSalary;
    }

    public String getMainSalary() {
        return mainSalary;
    }

    public String getActualSalary() {
        return actualSalary;
    }
    public EmployeeSalary(int employeeID, String mainSalary, String actualSalary, String differenceAmount, String type, String reason) {
        this.employeeID = employeeID;
        this.mainSalary = mainSalary;
        this.actualSalary = actualSalary;
        this.differenceAmount = differenceAmount;
        this.type = type;
        this.reason = reason;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public String getDifferenceAmount() {
        return differenceAmount;
    }

    public String getType() {
        return type;
    }

    public String getReason() {
        return reason;
    }
}