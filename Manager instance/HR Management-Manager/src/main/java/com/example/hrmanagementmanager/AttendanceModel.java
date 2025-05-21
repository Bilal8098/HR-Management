package com.example.hrmanagementmanager;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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

  public String getAttendDate() {
    if (attendDate != null) {
        LocalDateTime ldt = attendDate.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return ldt.format(formatter);
    }
    return "";
}


public void setAttendDate(Timestamp attendDate) {
    if (attendDate != null) {
        LocalDateTime truncatedDateTime = attendDate.toLocalDateTime().truncatedTo(ChronoUnit.MINUTES);
        this.attendDate = Timestamp.valueOf(truncatedDateTime);
    } else {
        this.attendDate = null;
    }
}
}