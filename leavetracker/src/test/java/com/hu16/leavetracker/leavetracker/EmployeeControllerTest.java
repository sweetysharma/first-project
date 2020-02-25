package com.hu16.leavetracker.leavetracker;

import com.hu16.leavetracker.leavetracker.controller.employee.EmployeeController;
import com.hu16.leavetracker.leavetracker.model.Employee;
import com.hu16.leavetracker.leavetracker.repository.EmployeeRepository;
import com.hu16.leavetracker.leavetracker.service.employee.EmployeeService;
import com.hu16.leavetracker.leavetracker.service.employee.Gender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
@RunWith(MockitoJUnitRunner.class)
public class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public  void setup(){
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(employeeController)
                .setControllerAdvice()
                .build();
    }

    @Test
    public void addEmployee() throws Exception {
        Employee employee = new Employee(1,"abhi",0, LocalDate.now(), Gender.MALE);
        when(employeeService.addEmployee(any())).thenReturn("string");
        MvcResult mvcResult = mockMvc.perform(post("/add"))
                .andExpect(status().isOk())
                .andReturn();
    }
}
*/