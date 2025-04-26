package com.example.hrmanagmenthr;

import javafx.beans.property.*;

public class Employee {
    private final IntegerProperty id;
    private final StringProperty fullName;
    private final StringProperty email;
    private final StringProperty phone;
    private final StringProperty address;
    private final DoubleProperty salary;
    private final StringProperty password;
    private final ObjectProperty<byte[]> fingerprint;

    // Constructor to initialize properties
    public Employee(int id, String fullName, String email, String phone, String address, double salary, String password, byte[] fingerprint) {
        this.id = new SimpleIntegerProperty(id);
        this.fullName = new SimpleStringProperty(fullName);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
        this.address = new SimpleStringProperty(address);
        this.salary = new SimpleDoubleProperty(salary);
        this.password = new SimpleStringProperty(password);
        this.fingerprint = new SimpleObjectProperty<>(fingerprint);
    }

    // Getter methods
    public int getId() { return id.get(); }
    public String getFullName() { return fullName.get(); }
    public String getEmail() { return email.get(); }
    public String getPhone() { return phone.get(); }
    public String getAddress() { return address.get(); }
    public double getSalary() { return salary.get(); }
    public String getPassword() { return password.get(); }
    public byte[] getFingerprint() { return fingerprint.get(); }

    // Setter methods
    public void setFullName(String fullName) { this.fullName.set(fullName); }
    public void setEmail(String email) { this.email.set(email); }
    public void setPhone(String phone) { this.phone.set(phone); }
    public void setAddress(String address) { this.address.set(address); }
    public void setSalary(double salary) { this.salary.set(salary); }
    public void setPassword(String password) { this.password.set(password); }
    public void setFingerprint(byte[] fingerprint) { this.fingerprint.set(fingerprint); }

    // Property methods for binding
    public IntegerProperty idProperty() { return id; }
    public StringProperty fullNameProperty() { return fullName; }
    public StringProperty emailProperty() { return email; }
    public StringProperty phoneProperty() { return phone; }
    public StringProperty addressProperty() { return address; }
    public DoubleProperty salaryProperty() { return salary; }
    public StringProperty passwordProperty() { return password; }
    public ObjectProperty<byte[]> fingerprintProperty() { return fingerprint; }
}
