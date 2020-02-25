package com.hu16.leavetracker.leavetracker.service.leave;

import com.hu16.leavetracker.leavetracker.Exception.LeaveException;
import com.hu16.leavetracker.leavetracker.controller.leave.LeaveController;
import com.hu16.leavetracker.leavetracker.model.Employee;
import com.hu16.leavetracker.leavetracker.model.LeaveRequest;
import com.hu16.leavetracker.leavetracker.repository.EmployeeRepository;
import com.hu16.leavetracker.leavetracker.repository.LeaveRepository;
import com.hu16.leavetracker.leavetracker.service.employee.EmployeeService;
import com.hu16.leavetracker.leavetracker.service.employee.Gender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.StrictMath.abs;


@Service
public class LeaveService {

    private static final Logger logger = LoggerFactory.getLogger(LeaveController.class);
    private LeaveRepository leaveRepository;
    private EmployeeRepository employeeRepository;
    private EmployeeService employeeService;


    @Autowired
    public LeaveService(EmployeeRepository employeeRepository, LeaveRepository leaveRepository, EmployeeService employeeService) {

        this.employeeRepository = employeeRepository;
        this.leaveRepository = leaveRepository;
        this.employeeService = employeeService;
    }



    public String addLeaveRequest(LeaveRequest leaveRequest) throws LeaveException {

        leaveRepository.save(leaveRequest);
        logger.info("Applying a leave request");
        return "Leave applied";
    }

    public LeaveRequest getLeaveDetailsById(int requestId) throws LeaveException {

        return leaveRepository.findById(requestId).get();
    }

    public List<LeaveRequest> getLeaveDetails() throws LeaveException {
        List<LeaveRequest> leaveRequest = new ArrayList<>();
        leaveRepository.findAll().forEach(leaveRequest::add);
        return leaveRequest;
    }

    public String deleteLeaveRequestById(int requestId) throws LeaveException {
        leaveRepository.deleteById(requestId);
        return "Deleted";
    }

    public String deleteAllLeaveRequest() throws LeaveException {
        leaveRepository.deleteAll();
        return "All leave request Deleted";
    }

    public List<LeaveRequest> getLeaveRequestsByStatus(LeaveStatus leaveStatus) {
        return leaveRepository.findByLeaveStatus(leaveStatus);
    }

    /*
        Find number of days between two dates
    */

    long daysBetween(LocalDate startDate, LocalDate endDate) {

        return abs(ChronoUnit.DAYS.between(startDate, endDate));

    }

        /*
            Find the difference between two dates excluding weekends(Saturday or Sunday)
      */

    private int differenceByExcludingWeekend(LocalDate startDate, LocalDate endDate) {

        int count = 0;

        //  Iterate over startDate through endDate
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                continue;
            } else {
                count++;
            }
        }
        return count;
    }

    private boolean isNonBlanketCoverageLeave(LeaveRequest leaveRequest) {
        return (leaveRequest.getLeaveType() != LeaveType.SABBATICAL &&
                leaveRequest.getLeaveType() != LeaveType.MATERNITY && leaveRequest.getLeaveType() != LeaveType.PATERNITY);
    }

    private boolean leaveDatesOverlap(LeaveRequest leaveRequest, LeaveRequest leaveRequest2) {
        return leaveRequest.getStartDate().isBefore(leaveRequest2.getEndDate())
                && leaveRequest2.getStartDate().isBefore(leaveRequest.getEndDate());
    }

    /*
        Function to check the paternity leave can be applied or not
    */

    public boolean checkForPaternityLeave(int employeeId) throws LeaveException {

        List<LeaveRequest> leaveRequests = new ArrayList<>();
        leaveRequests = getLeaveDetails();
        int count = 0;
        for (LeaveRequest temp : leaveRequests) {
            if (temp.getEmployeeId() == employeeId && temp.getLeaveType() == LeaveType.PATERNITY && temp.getLeaveStatus() == LeaveStatus.APPROVE)
                count++;
        }
        if (count >= 2)
           return false;
       else
          return true;

    }

    /*
        Function to check the maternity leave can be applied or not
    */

    public boolean checkForMaternityLeave(int employeeId) throws LeaveException {

        List<LeaveRequest> leaveRequests = new ArrayList<>();
        leaveRequests = getLeaveDetails();
        int count = 0;
        for (LeaveRequest temp : leaveRequests) {
            if (temp.getEmployeeId() == employeeId && temp.getLeaveType() == LeaveType.MATERNITY && temp.getLeaveStatus() == LeaveStatus.APPROVE)
                count++;
        }
        if (count >= 2)
            return false;
        else
            return true;
    }

    // Get existing leave requests of the employee which are PENDING or APPROVED

    private LeaveRequest leaveRequestForEmployee(int employeeId) throws LeaveException {
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        leaveRequests = getLeaveDetails();
        try {
            return leaveRequests.stream().filter(request -> request.getEmployeeId() == employeeId
                    && request.getLeaveStatus()
                    == LeaveStatus.PENDING).collect(Collectors.toCollection(ArrayList::new)).get(0);
        } catch (Exception e) {
            return null;
        }
    }


    /*
        submit a leave request  :  Core logic to handle all the scenario that leave request will approve or reject.
    */

    public LeaveResponse applyLeave(LeaveRequest leaveRequest) throws LeaveException {

        boolean flag = true;    //Used to check that paternity or maternity leave will approve or not
        int daysBetween = (int) daysBetween(leaveRequest.getStartDate(), leaveRequest.getEndDate());

        // exclude the weekend
        if(leaveRequest.getLeaveType() != LeaveType.MATERNITY && leaveRequest.getLeaveType() != LeaveType.SABBATICAL) {
               daysBetween = daysBetween - differenceByExcludingWeekend(leaveRequest.getStartDate(), leaveRequest.getEndDate());
        }
        Employee employee = employeeService.getEmployeeById(1);
        int employeeLeaveBalance = employee.getLeaveBalance();
        Gender employeeGender = employee.getGender();
        LeaveType leaveType = leaveRequest.getLeaveType();
        LocalDate startDate = leaveRequest.getStartDate();
        LocalDate endDate = leaveRequest.getEndDate();
        LocalDate employeeJoiningDate = employee.getJoiningDate();
        LeaveResponse leaveResponse;
        if (daysBetween > 0) {

            // Existing leave requests of this employee
            LeaveRequest existingLeaveRequest = leaveRequestForEmployee(leaveRequest.getEmployeeId());

            // Check if low leave balance (pass check for non blanket coverage leaves)
            if (daysBetween > employeeLeaveBalance && isNonBlanketCoverageLeave(leaveRequest)) {

                leaveRequest.setLeaveStatus(LeaveStatus.REJECT);
                leaveRepository.save(leaveRequest);
                return new LeaveResponse(LeaveStatus.REJECT, "Insufficient leave balance");

                // If any existing leave requests which are not blanket coverage
            } else if (existingLeaveRequest != null && isNonBlanketCoverageLeave(leaveRequest)) {

                // If any existing requests by same employee and leave dates overlap
                if (!leaveDatesOverlap(existingLeaveRequest, leaveRequest)) {
                    leaveRepository.save(leaveRequest);
                    return new LeaveResponse(LeaveStatus.PENDING, "Leave request applied successfully.");
                } else {
                    leaveRequest.setLeaveStatus(LeaveStatus.REJECT);
                    leaveRepository.save(leaveRequest);
                    return new LeaveResponse(LeaveStatus.REJECT, "Leave requests already pending for the employee.");
                }

                // If FEMALE employee applies for PATERNITY leave
            } else if (employeeGender == Gender.FEMALE && leaveType == LeaveType.PATERNITY) {
                leaveRequest.setLeaveStatus(LeaveStatus.REJECT);
                leaveRepository.save(leaveRequest);
                return new LeaveResponse(LeaveStatus.REJECT, "Female employees cannot apply for paternity leave");

                // If MALE employee applies for MATERNITY leave
            } else if (employeeGender == Gender.MALE && leaveType == LeaveType.MATERNITY) {
                leaveRequest.setLeaveStatus(LeaveStatus.REJECT);
                leaveRepository.save(leaveRequest);
                return new LeaveResponse(LeaveStatus.REJECT, "Maternity leave is no applicable for male employees");

                // If FEMALE employee applies for MATERNITY leave
            } else if (employeeGender == Gender.FEMALE && leaveType == LeaveType.MATERNITY) {
                // Check if previous maternity leave exists, its count & difference with applying date
                flag = checkForMaternityLeave(leaveRequest.getEmployeeId());

                if (flag == false) {          // If 2 maternity leaves already used
                    leaveRequest.setLeaveStatus(LeaveStatus.REJECT);
                    leaveRepository.save(leaveRequest);
                    return new LeaveResponse(LeaveStatus.REJECT, "You have already used 2 maternity leaves");
                } else {                                            // Apply maternity leave
                    // Check if 80 days service period is completed before approving maternity leave
                    if (ChronoUnit.DAYS.between(employeeJoiningDate, startDate) >= 80) {
                        leaveRequest.setEndDate(startDate.plusMonths(6));     // Add 6 month to start date
                        leaveRequest.setLeaveStatus(LeaveStatus.APPROVE);
                        leaveRepository.save(leaveRequest);
                        return new LeaveResponse(LeaveStatus.APPROVE, "Maternity leave successfully applied.");
                    } else {
                        leaveRequest.setLeaveStatus(LeaveStatus.REJECT);
                        leaveRepository.save(leaveRequest);
                        return new LeaveResponse(LeaveStatus.REJECT, "80 days service required before applying for MATERNITY leave");
                    }
                }
                // If MALE employee applies for PATERNITY leave
            } else if (employeeGender == Gender.MALE && leaveType == LeaveType.PATERNITY) {
                // Check if previous PATERNITY leave exists, its count & difference with applying date
                flag = checkForPaternityLeave(leaveRequest.getEmployeeId());
                if (flag == false) {          // If 2 PATERNITY leaves already used
                    leaveRequest.setLeaveStatus(LeaveStatus.REJECT);
                    leaveRepository.save(leaveRequest);
                    return new LeaveResponse(LeaveStatus.REJECT, "Paternity leave successfully applied.");
                } else {                                            // Apply PATERNITY leave
                    leaveRequest.setEndDate(startDate.plusMonths(6));     // Add 1 month to start date
                    leaveRequest.setLeaveStatus(LeaveStatus.APPROVE);
                    this.leaveRepository.save(leaveRequest);
                    return new LeaveResponse(LeaveStatus.APPROVE,"Paternity leave successfully applied.");
                }
            } else if (leaveType == LeaveType.SABBATICAL && daysBetween <= employee.getLeaveBalance()) {       // For SABBATICAL leave
                employee.setLeaveBalance(employee.getLeaveBalance() - daysBetween);
                leaveRequest.setLeaveStatus(LeaveStatus.APPROVE);
                leaveRepository.save(leaveRequest);
                return new LeaveResponse(LeaveStatus.APPROVE, "Leave  applied successfully.");
            } else if (daysBetween > employee.getLeaveBalance())                                                           // Add the request as PENDING
            {
                leaveRequest.setLeaveStatus(LeaveStatus.REJECT);
                leaveRepository.save(leaveRequest);
                return new LeaveResponse(LeaveStatus.REJECT, "You don't have the sufficient leave balance");

            } else {
                leaveRequest.setLeaveStatus(LeaveStatus.PENDING);
                this.leaveRepository.save(leaveRequest);
                return new LeaveResponse(LeaveStatus.PENDING, "Leave applied successfully.");
            }

        } else {
            throw new IllegalArgumentException("Leaves can be applied only for Non holidays.");
        }

    }
}




