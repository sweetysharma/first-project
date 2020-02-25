package com.hu16.leavetracker.leavetracker.repository;

import com.hu16.leavetracker.leavetracker.model.LeaveRequest;
import com.hu16.leavetracker.leavetracker.service.leave.LeaveStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LeaveRepository extends CrudRepository<LeaveRequest,Integer> {

    List<LeaveRequest> findByLeaveStatus(LeaveStatus leaveStatus);

    List<LeaveRequest> findByEmployeeId(int employeeId);

}
