package com.hu16.leavetracker.leavetracker.model;



import com.hu16.leavetracker.leavetracker.service.leave.LeaveStatus;
import com.hu16.leavetracker.leavetracker.service.leave.LeaveType;

import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "compoffleaverequests")
public class CompOffLeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long requestId;
    private int employeeId;
    private LocalDate leaveDate;
    private LocalDate compDate;
    private LeaveType leaveType;
    private LeaveStatus leaveStatus;
    private int compOffBalance;

    public CompOffLeaveRequest() {
    }

    public CompOffLeaveRequest(int employeeId, LocalDate leaveDate, LocalDate compDate, LeaveType leaveType,
                               LeaveStatus leaveStatus,int compOffBalance) {
        this.employeeId = employeeId;
        this.leaveDate = leaveDate;
        this.compDate = compDate;
        this.leaveType = leaveType;
        this.leaveStatus = leaveStatus;
        this.compOffBalance = compOffBalance;
    }


    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(LocalDate leaveDate) {
        this.leaveDate = leaveDate;
    }

    public LocalDate getCompDate() {
        return compDate;
    }

    public void setCompDate(LocalDate compDate) {
        this.compDate = compDate;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public LeaveStatus getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(LeaveStatus leaveStatus) {
        this.leaveStatus = leaveStatus;
    }
    public int getCompOffBalance() {
        return this.compOffBalance;
    }
    public void setCompOffBalance(int compOffBalance){
        this.compOffBalance = this.compOffBalance + compOffBalance;
    }
}
