package com.hu16.leavetracker.leavetracker.model;

import com.hu16.leavetracker.leavetracker.service.leave.LeaveStatus;
import com.hu16.leavetracker.leavetracker.service.leave.LeaveType;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Table(name ="leaverequest")
public class LeaveRequest {

    @Id
    private int requestId;
    private int employeeId;
    private LeaveStatus leaveStatus;
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;

    public LeaveRequest() {

    }

    public LeaveRequest(int requestId , int employeeId , LeaveStatus leaveStatus , LeaveType leaveType , LocalDate startDate , LocalDate endDate) {

        this.requestId = requestId;
        this.employeeId = employeeId;
        this.leaveStatus = leaveStatus;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;

    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public LeaveType getLeaveType() {
        return this.leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }


    public LeaveStatus getLeaveStatus() {
        return this.leaveStatus;
    }

    public void setLeaveStatus(LeaveStatus leaveStatus) {
        this.leaveStatus = leaveStatus;
    }



}
