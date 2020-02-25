package com.hu16.leavetracker.leavetracker.repository;

import com.hu16.leavetracker.leavetracker.model.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

}