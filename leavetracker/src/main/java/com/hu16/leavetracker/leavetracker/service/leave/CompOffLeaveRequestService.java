package com.hu16.leavetracker.leavetracker.service.leave;

import com.hu16.leavetracker.leavetracker.controller.leave.LeaveController;
import com.hu16.leavetracker.leavetracker.model.CompOffLeaveRequest;
import com.hu16.leavetracker.leavetracker.model.WorkHoursLog;
import com.hu16.leavetracker.leavetracker.repository.CompOffLeaveRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static java.lang.StrictMath.abs;

@Service
public class CompOffLeaveRequestService {

    private static final Logger logger = LoggerFactory.getLogger(LeaveController.class);
    private CompOffLeaveRepository compOffLeaveRepository;

    public CompOffLeaveRequestService (CompOffLeaveRepository compOffLeaveRepository) {

        this.compOffLeaveRepository = compOffLeaveRepository;

    }

    public int findHours(WorkHoursLog workHoursLog) {

        int startHour , startMinute , endHour , endMinute ,totalHours;
        String []str = workHoursLog.getStartTime().split(":");
        startHour = Integer.parseInt(str[0]);
        startMinute = Integer.parseInt(str[1]);
        str = workHoursLog.getEndTime().split(":");
        endHour = Integer.parseInt(str[0]);
        endMinute = Integer.parseInt(str[1]);
        totalHours = endHour - startHour;
        return totalHours;


    }
    public void updateCompOffBalance (WorkHoursLog workHoursLog, CompOffLeaveRequest compOffLeaveRequest) {

        int hours = findHours(workHoursLog);
        if(hours >= 8)
            compOffLeaveRequest.setCompOffBalance(1);
        return;

    }

    /*
        Find number of days between two dates
    */

    long daysBetween(LocalDate startDate, LocalDate endDate) {

        return abs(ChronoUnit.DAYS.between(startDate, endDate));

    }
    public LeaveStatus applyCompOffLeaveRequest(CompOffLeaveRequest compOffLeaveRequest) {

//        int daysBetween = daysBetween(compOffLeaveRequest.getLeaveDate() . compOffLeaveRequest.getCompDate());
//        if(compOffLeaveRequest.getCompOffBalance() <daysBetween )
//            return LeavesStatus.REJECT;
        return LeaveStatus.APPROVE;

    }

}
