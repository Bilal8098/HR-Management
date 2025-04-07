package com.example.hrmanagmenthr;

public class Employee {
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private double salary;
    private String password;
    private byte[] fingerprint;

    public Employee(int id, String fullName, String email, String phone, String address, double salary, String password, byte[] fingerprint) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.salary = salary;
        this.password = password;
        this.fingerprint = fingerprint;
    }

    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public byte[] getFingerprint() { return fingerprint; }
}
