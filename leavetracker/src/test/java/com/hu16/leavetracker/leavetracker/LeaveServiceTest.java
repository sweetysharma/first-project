package com.hu16.leavetracker.leavetracker;

import com.hu16.leavetracker.leavetracker.Exception.LeaveException;
import com.hu16.leavetracker.leavetracker.model.Employee;
import com.hu16.leavetracker.leavetracker.model.LeaveRequest;
import com.hu16.leavetracker.leavetracker.repository.EmployeeRepository;
import com.hu16.leavetracker.leavetracker.repository.LeaveRepository;
import com.hu16.leavetracker.leavetracker.service.employee.EmployeeService;
import com.hu16.leavetracker.leavetracker.service.employee.Gender;
import com.hu16.leavetracker.leavetracker.service.leave.LeaveResponse;
import com.hu16.leavetracker.leavetracker.service.leave.LeaveService;
import com.hu16.leavetracker.leavetracker.service.leave.LeaveStatus;
import com.hu16.leavetracker.leavetracker.service.leave.LeaveType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LeaveServiceTest {

    @InjectMocks
    private LeaveService leaveService;
    private Employee employee;
    private LeaveRequest leaveRequest;
    private LeaveResponse leaveResponse;


    @Mock
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private LeaveRepository leaveRepository;


    /*
        OOO Leave should be rejected because of insufficient leave balance
     */

    @Test
    public void leaveRequestRejected_InsuffcientLeaveBalance_test() throws LeaveException {

        leaveService = new LeaveService(employeeRepository, leaveRepository, employeeService);
        employee = new Employee(1, "abhishek", 2, LocalDate.now(), Gender.MALE);
        leaveRequest = new LeaveRequest(2, 1, LeaveStatus.PENDING, LeaveType.OOO, LocalDate.of(2019, 8, 17), LocalDate.of(2019, 8, 30));
        when(employeeService.getEmployeeById(1)).thenReturn(employee);
        leaveResponse = leaveService.applyLeave(leaveRequest);
        Assert.assertEquals("expect leave request status to be REJECTED.", LeaveStatus.REJECT, leaveResponse.getLeaveStatus());

    }


    /*
        SABBATICAL leave Approve because employee have sufficient leave balance to take
    */

    @Test
    public void leaveRequestApprove_suffcientLeaveBalance_test() throws LeaveException {

        leaveService = new LeaveService(employeeRepository, leaveRepository, employeeService);
        employee = new Employee(1, "abhishek", 10, LocalDate.now(), Gender.MALE);
        leaveRequest = new LeaveRequest(2, 1, LeaveStatus.PENDING, LeaveType.SABBATICAL, LocalDate.of(2019, 8, 22), LocalDate.of(2019, 8, 30));
        when(employeeService.getEmployeeById(1)).thenReturn(employee);
        leaveResponse = leaveService.applyLeave(leaveRequest);
        Assert.assertEquals("expect leave request status to be Approved.", LeaveStatus.APPROVE, leaveResponse.getLeaveStatus());

    }

    /*
        SABBATICAL leave rejected because employee have insufficient leave balance
    */

    @Test
    public void leaveRequestRejected_insuffcientLeaveBalance_test() throws LeaveException {

        leaveService = new LeaveService(employeeRepository, leaveRepository, employeeService);
        employee = new Employee(1, "abhishek", 2, LocalDate.now(), Gender.MALE);
        leaveRequest = new LeaveRequest(2, 1, LeaveStatus.PENDING, LeaveType.SABBATICAL, LocalDate.of(2019, 8, 17), LocalDate.of(2019, 8, 30));
        when(employeeService.getEmployeeById(1)).thenReturn(employee);
        leaveResponse = leaveService.applyLeave(leaveRequest);
        Assert.assertEquals("expect leave request status to be Approved.", LeaveStatus.REJECT, leaveResponse.getLeaveStatus());

    }

    /*
        Reject if Female employee apply for paternity leave
    */

    @Test
    public void rejectPaternityLeave_ForFemale_test() throws LeaveException {

        leaveService = new LeaveService(employeeRepository, leaveRepository, employeeService);
        employee = new Employee(1, "swity", 2, LocalDate.now(), Gender.FEMALE);
        leaveRequest = new LeaveRequest(2, 1, LeaveStatus.PENDING, LeaveType.PATERNITY, LocalDate.of(2019, 8, 17), LocalDate.of(2019, 8, 30));
        when(employeeService.getEmployeeById(1)).thenReturn(employee);
        LeaveResponse leaveResponse = leaveService.applyLeave(leaveRequest);
        Assert.assertEquals("expect leave request status to be Approved.", LeaveStatus.REJECT, leaveResponse.getLeaveStatus());

    }

    /*
        Reject if male employee apply for maternity leave
    */

    @Test
    public void rejectIfMaleApplyForMaternity_test() throws LeaveException {

        leaveService = new LeaveService(employeeRepository, leaveRepository, employeeService);
        employee = new Employee(1, "abhishek", 2, LocalDate.now(), Gender.MALE);
        leaveRequest = new LeaveRequest(2, 1, LeaveStatus.PENDING, LeaveType.MATERNITY, LocalDate.of(2019, 8, 17), LocalDate.of(2019, 8, 30));
        when(employeeService.getEmployeeById(1)).thenReturn(employee);
        leaveResponse = leaveService.applyLeave(leaveRequest);
        Assert.assertEquals("expect leave request status to be Approved.", LeaveStatus.REJECT, leaveResponse.getLeaveStatus());

    }



    /*
        Reject if female employee apply for maternity leave before 80 working days
    */

    @Test
    public void rejectFemaleApplyBeforeSerice_test() throws LeaveException {

        leaveService = new LeaveService(employeeRepository, leaveRepository, employeeService);
        employee = new Employee(1, "swity", 2, LocalDate.now(), Gender.FEMALE);
        leaveRequest = new LeaveRequest(2, 1, LeaveStatus.PENDING, LeaveType.MATERNITY, LocalDate.of(2019, 8, 17), LocalDate.of(2019, 8, 30));
        when(employeeService.getEmployeeById(1)).thenReturn(employee);
        leaveResponse = leaveService.applyLeave(leaveRequest);
        Assert.assertEquals("expect leave request status to be Rejected.", LeaveStatus.REJECT, leaveResponse.getLeaveStatus());

    }


    /*
        Approve if female employee apply for maternity leave after 80 working days
    */

    @Test
    public void approveMaternityTest() throws LeaveException {

        leaveService = new LeaveService(employeeRepository, leaveRepository, employeeService);
        employee = new Employee(1, "swity", 2, LocalDate.now().minusMonths(3), Gender.FEMALE);
        leaveRequest = new LeaveRequest(2, 1, LeaveStatus.PENDING, LeaveType.MATERNITY, LocalDate.of(2019, 8, 17), LocalDate.of(2019, 8, 30));
        when(employeeService.getEmployeeById(1)).thenReturn(employee);
        leaveResponse = leaveService.applyLeave(leaveRequest);
        Assert.assertEquals("expect leave request status to be Rejected.", LeaveStatus.APPROVE, leaveResponse.getLeaveStatus());

    }


    /*
        Approve if male employee apply for paternity leave
    */

    @Test
    public void approvePaternityLeaveTest() throws LeaveException {

        leaveService = new LeaveService(employeeRepository, leaveRepository, employeeService);
        employee = new Employee(1, "abhishek", 2, LocalDate.now(), Gender.MALE);
        leaveRequest = new LeaveRequest(2, 1, LeaveStatus.PENDING, LeaveType.PATERNITY, LocalDate.of(2019, 8, 17), LocalDate.of(2019, 8, 30));
        when(employeeService.getEmployeeById(1)).thenReturn(employee);
        leaveResponse = leaveService.applyLeave(leaveRequest);
        Assert.assertEquals("expect leave request status to be Approved.", LeaveStatus.APPROVE, leaveResponse.getLeaveStatus());

    }


    /*
        Reject if male employee apply for paternity leave more than 2 times
    */

    @Test
    public void rejectIfApplyPaternityMoreThan2Time_test() throws LeaveException {

        leaveService = new LeaveService(employeeRepository, leaveRepository, employeeService);
        employee = new Employee(1, "abhishek", 2, LocalDate.now(), Gender.MALE);
        when(employeeService.getEmployeeById(1)).thenReturn(employee);

        List<LeaveRequest> leaveRequests = new ArrayList<>();
        leaveRequest = new LeaveRequest(1, 1, LeaveStatus.PENDING, LeaveType.PATERNITY, LocalDate.of(2019, 8, 17), LocalDate.of(2019, 8, 27));
        leaveRequests.add(leaveRequest);
        leaveRequest = new LeaveRequest(3, 1, LeaveStatus.PENDING, LeaveType.PATERNITY, LocalDate.of(2019, 9, 1), LocalDate.of(2020, 9, 10));
        leaveRequests.add(leaveRequest);
        leaveRequest = new LeaveRequest(4, 1, LeaveStatus.PENDING, LeaveType.PATERNITY, LocalDate.of(2019, 9, 15), LocalDate.of(2021, 9, 25));
        leaveRequests.add(leaveRequest);

        when(leaveService.getLeaveDetails()).thenReturn(leaveRequests);

        leaveResponse = leaveService.applyLeave(leaveRequests.get(0));
        Assert.assertEquals("expect leave request status to be Approved.", LeaveStatus.APPROVE, leaveResponse.getLeaveStatus());

        leaveResponse = leaveService.applyLeave(leaveRequests.get(1));
        Assert.assertEquals("expect leave request status to be Approved.", LeaveStatus.APPROVE, leaveResponse.getLeaveStatus());

        leaveResponse = leaveService.applyLeave(leaveRequests.get(2));
        Assert.assertEquals("expect leave request status to be Rejected.", LeaveStatus.REJECT, leaveResponse.getLeaveStatus());

    }

    /*
            Reject leave request if female employee apply maternity leave more than 2
     */

    @Test
    public void rejectIfApplyMaternityMoreThan2Times_test() throws LeaveException {

        leaveService = new LeaveService(employeeRepository, leaveRepository, employeeService);
        employee = new Employee(1, "swity", 2, LocalDate.now().minusMonths(7), Gender.FEMALE);
        when(employeeService.getEmployeeById(1)).thenReturn(employee);

        List<LeaveRequest> leaveRequests = new ArrayList<>();
        leaveRequest = new LeaveRequest(1, 1, LeaveStatus.PENDING, LeaveType.MATERNITY, LocalDate.of(2019, 8, 17), LocalDate.of(2019, 8, 27));
        leaveRequests.add(leaveRequest);
        leaveRequest = new LeaveRequest(3, 1, LeaveStatus.PENDING, LeaveType.MATERNITY, LocalDate.of(2020, 9, 17), LocalDate.of(2020, 9, 10));
        leaveRequests.add(leaveRequest);
        leaveRequest = new LeaveRequest(4, 1, LeaveStatus.PENDING, LeaveType.MATERNITY, LocalDate.of(2021, 9, 15), LocalDate.of(2021, 9, 25));
        leaveRequests.add(leaveRequest);

        when(leaveService.getLeaveDetails()).thenReturn(leaveRequests);

        leaveResponse = leaveService.applyLeave(leaveRequests.get(0));
        Assert.assertEquals("expect leave request status to be Approved.", LeaveStatus.APPROVE, leaveResponse.getLeaveStatus());

        leaveResponse = leaveService.applyLeave(leaveRequests.get(1));
        Assert.assertEquals("expect leave request status to be Approved.", LeaveStatus.APPROVE, leaveResponse.getLeaveStatus());

        leaveResponse = leaveService.applyLeave(leaveRequests.get(2));
        Assert.assertEquals("expect leave request status to be Rejected.", LeaveStatus.REJECT, leaveResponse.getLeaveStatus());

    }


    /*
        Reject for overlap leave request
    */

    @Test
    public void rejectForOverlappingRequest_test() throws LeaveException {

        List<LeaveRequest> leaveRequests = new ArrayList<>();
        leaveService = new LeaveService(employeeRepository, leaveRepository, employeeService);
        employee = new Employee(1, "abhishek", 10, LocalDate.now(), Gender.MALE);
        when(employeeService.getEmployeeById(1)).thenReturn(employee);

        leaveRequest = new LeaveRequest(1, 1, LeaveStatus.APPROVE, LeaveType.SABBATICAL, LocalDate.of(2019, 8, 17), LocalDate.of(2019, 8, 25));
        leaveRequests.add(leaveRequest);
        leaveResponse = leaveService.applyLeave(leaveRequests.get(0));
        Assert.assertEquals("expect leave request status to be Approved.", LeaveStatus.APPROVE, leaveResponse.getLeaveStatus());


        leaveRequest = new LeaveRequest(2, 1, LeaveStatus.PENDING, LeaveType.SABBATICAL, LocalDate.of(2019, 8, 20), LocalDate.of(2019, 8, 27));
        leaveRequests.add(leaveRequest);
        leaveResponse = leaveService.applyLeave(leaveRequests.get(1));
        Assert.assertEquals("expect leave request status to be Rejected.", LeaveStatus.REJECT, leaveResponse.getLeaveStatus());

    }



}