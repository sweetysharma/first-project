package com.hu16.leavetracker.leavetracker.model;


import com.hu16.leavetracker.leavetracker.log.LogType;

import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "worklogs")
public class WorkHoursLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long logId;
    private int employeeId;
    private LocalDate date;
    private String startTime;
    private String endTime;
    private LogType logType;

    public WorkHoursLog() {
    }

    public WorkHoursLog(int logId,int employeeId, LocalDate date, String startTime, String endTime, LogType logType) {
        this.logId = logId;
        this.employeeId = employeeId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.logType = logType;
    }

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }


    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }
}
