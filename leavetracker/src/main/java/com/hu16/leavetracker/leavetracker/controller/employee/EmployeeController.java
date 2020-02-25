package com.hu16.leavetracker.leavetracker.controller.employee;

import com.hu16.leavetracker.leavetracker.Exception.LeaveException;
import com.hu16.leavetracker.leavetracker.controller.leave.LeaveController;
import com.hu16.leavetracker.leavetracker.model.Employee;
import com.hu16.leavetracker.leavetracker.service.employee.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {
    private static final Logger logger  = LoggerFactory.getLogger(LeaveController.class);
    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /*
        Adding new employee
     */
    @PostMapping("/add")
    public  ResponseEntity addEmployee(@RequestBody Employee employeeEntity)throws LeaveException {
        logger.info("Adding an employee ");
        String message = employeeService.addEmployee(employeeEntity);
        return new ResponseEntity(message, HttpStatus.OK);
    }

    /*
        Fetching the employee details
     */
    @RequestMapping(value = "/employee/{id}",method = RequestMethod.GET)
    public ResponseEntity getEmployeeById(@PathVariable("id") int employeeId) throws LeaveException{
        logger.info("Fetching user details");
        Employee employee = employeeService.getEmployeeById(employeeId);
        if(employee == null)
            throw new LeaveException("Bad fetch");
        else
            return new ResponseEntity(employee, HttpStatus.OK);
    }

    /*
    Fetching all employee details
 */
    @RequestMapping(value = "/employee",method = RequestMethod.GET)
    public ResponseEntity getAllEmployee() throws LeaveException{
        logger.info("Fetching All Employee details");
        List<Employee> employee = employeeService.getAllEmployee();
        if(employee == null)
            throw new LeaveException("Bad fetch");
        else
            return new ResponseEntity(employee, HttpStatus.OK);
    }

    /*
        Deleting the employee by Id
     */
    @RequestMapping(value = "employee/{id}",method = RequestMethod.DELETE)
    public ResponseEntity deleteEmployeeById(@PathVariable("id") int employeeId) throws LeaveException {
        logger.info("Deleting user details");
        String message = employeeService.deleteEmployeeById(employeeId);
        return new ResponseEntity(message, HttpStatus.OK);

    }


        /*
        Deleting the All employe
     */
    @RequestMapping(value = "employee",method = RequestMethod.DELETE)
    public ResponseEntity deleteAllEmployee() throws LeaveException {
        logger.info("Deleting all the employee details");
        String message = employeeService.deleteAllEmployee();
        return new ResponseEntity(message, HttpStatus.OK);

    }

    /*
    Get employee leave balance by id
     */
    @RequestMapping(value = "leavebalance/{id}",method = RequestMethod.GET)
    public int getEmployeeLeaveBalance(@PathVariable("id") int employeeId)throws LeaveException {
        logger.info("Fetching leave balance of employee through request id");
        return employeeService.getEmployeeLeaveBalance(employeeId);
    }

    }




