package com.hu16.leavetracker.leavetracker.model;

import com.hu16.leavetracker.leavetracker.service.employee.Gender;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static java.lang.StrictMath.abs;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    private int employeeId;
    private String employeeName;
    private int leaveBalance;
    private LocalDate joiningDate;
    private Gender gender;

    public Employee(int employeeId, String employeeName , int leaveBalance   , LocalDate joiningDate  ,  Gender gender) {

        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.leaveBalance = leaveBalance;
        this.joiningDate = joiningDate;
        this.gender = gender;

    }
    public Employee(int employeeId, String employeeName , LocalDate joiningDate  ,  Gender gender) {

        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.joiningDate = joiningDate;
        this.leaveBalance = updateLeaveBalance();
        this.gender = gender;
    }
    public int updateLeaveBalance() {

            LocalDate todayDate = LocalDate.now();
            if(todayDate.getDayOfMonth() <= 15)
                    return 1;
            else
                return 2;

    }
    public int checkForLeaveBalance(LocalDate asOfDate) {

            int diffOfMonth = (int)abs(ChronoUnit.MONTHS.between(joiningDate, asOfDate));
            this.leaveBalance = this.leaveBalance + (diffOfMonth * 2) ;
            return diffOfMonth;

    }

    public Employee() {
    }

    public int getEmployeeId() {
        return employeeId;
    }


    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return this.employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getLeaveBalance() {
        return this.leaveBalance;
    }

    public void setLeaveBalance(int leaveBalance) {
        this.leaveBalance = leaveBalance;
    }


    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
