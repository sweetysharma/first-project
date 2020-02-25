package com.hu16.leavetracker.leavetracker.service.employee;

import com.hu16.leavetracker.leavetracker.Exception.LeaveException;
import com.hu16.leavetracker.leavetracker.controller.leave.LeaveController;
import com.hu16.leavetracker.leavetracker.model.Employee;
import com.hu16.leavetracker.leavetracker.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    private static final Logger logger  = LoggerFactory.getLogger(LeaveController.class);
    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService (EmployeeRepository employeeRepository) {

        this.employeeRepository = employeeRepository;

    }
    public String addEmployee(Employee employee) throws LeaveException{


        employeeRepository.save(employee);
        logger.info("Employee joined");
        return "Saved";
    }

    public Employee getEmployeeById(int employeeId)throws LeaveException {
        logger.info("Fetching employee details by id");
        return employeeRepository.findById(employeeId).get();
    }


    public List<Employee> getAllEmployee()throws LeaveException {
        logger.info("Fetching all the employee details");
        List<Employee> employees = new ArrayList<>();
        employeeRepository.findAll().forEach(employees::add);
        return employees;
    }

    public String deleteEmployeeById(int employeeId)throws LeaveException {
        employeeRepository.deleteById(employeeId);
        return "Deleted";
    }

    public String deleteAllEmployee()throws LeaveException {
        logger.info("Deleting all the employee");
        employeeRepository.deleteAll();
        return "All employees Deleted";
    }

    public int getEmployeeLeaveBalance(int employeeId) {
        return employeeRepository.findById(employeeId).get().getLeaveBalance();
    }



}
