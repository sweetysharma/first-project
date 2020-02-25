package com.hu16.leavetracker.leavetracker.repository;


import com.hu16.leavetracker.leavetracker.model.CompOffLeaveRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompOffLeaveRepository extends CrudRepository<CompOffLeaveRequest, Long> {
}