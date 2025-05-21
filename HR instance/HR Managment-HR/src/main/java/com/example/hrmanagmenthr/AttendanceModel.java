package com.example.hrmanagmenthr;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

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

    // Modified getter returns formatted String without seconds
    public String getAttendDate() {
        if (attendDate == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(attendDate);
    }

    // Setter remains accepting Timestamp
    public void setAttendDate(Timestamp attendDate) {
        this.attendDate = attendDate;
    }
}
