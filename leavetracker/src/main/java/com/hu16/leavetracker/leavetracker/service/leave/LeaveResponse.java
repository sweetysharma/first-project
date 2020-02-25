package com.hu16.leavetracker.leavetracker.service.leave;

public class LeaveResponse {

    private LeaveStatus leaveStatus;
    private String message;

    public LeaveResponse (LeaveStatus leaveStatus , String message) {

        this.leaveStatus = leaveStatus;
        this.message = message;

    }

    public LeaveStatus getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(LeaveStatus leaveStatus) {
        this.leaveStatus = leaveStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
