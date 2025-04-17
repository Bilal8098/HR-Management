package com.example.hrmanagementmanager;


import java.sql.Timestamp;

public class AttendanceModel {
    private int attendanceId;
    private int employeeId;
    private Timestamp attendDate;

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Timestamp getAttendDate() {
        return attendDate;
    }

    public void setAttendDate(Timestamp attendDate) {
        this.attendDate = attendDate;
    }
}