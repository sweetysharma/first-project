package com.hu16.leavetracker.leavetracker.repository;


import com.hu16.leavetracker.leavetracker.log.LogType;
import com.hu16.leavetracker.leavetracker.model.WorkHoursLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LogRepository extends CrudRepository<WorkHoursLog, Long> {

    List<WorkHoursLog> findByEmployeeId(int employeeId);

    List<WorkHoursLog> findByLogType(LogType logType);

}
