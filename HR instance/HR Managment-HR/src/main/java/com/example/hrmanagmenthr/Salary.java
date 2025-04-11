package com.example.hrmanagmenthr;



public class Salary {
    private int employeeId;
    private double mainSalary;
    private double actualSalary;
    private double differenceAmount;
    private String type;
    private String reason;

    public Salary(int employeeId, double mainSalary, double actualSalary, double differenceAmount, String type, String reason) {
        this.employeeId = employeeId;
        this.mainSalary = mainSalary;
        this.actualSalary = actualSalary;
        this.differenceAmount = differenceAmount;
        this.type = type;
        this.reason = reason;
    }

    public int getEmployeeId() { return employeeId; }
    public double getMainSalary() { return mainSalary; }
    public double getActualSalary() { return actualSalary; }
    public double getDifferenceAmount() { return differenceAmount; }
    public String getType() { return type; }
    public String getReason() { return reason; }
}

