package com.hu16.leavetracker.leavetracker.controller.leave;

import com.hu16.leavetracker.leavetracker.Exception.LeaveException;
import com.hu16.leavetracker.leavetracker.model.LeaveRequest;
import com.hu16.leavetracker.leavetracker.repository.LeaveRepository;
import com.hu16.leavetracker.leavetracker.service.leave.LeaveService;
import com.hu16.leavetracker.leavetracker.service.leave.LeaveStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LeaveController {

    private static final Logger logger  = LoggerFactory.getLogger(LeaveController.class);
    private LeaveService leaveService;
    private LeaveRepository leaveRepository;

    @Autowired
    LeaveController(LeaveService leaveService ,LeaveRepository leaveRepository) {

        this.leaveService = leaveService;
        this.leaveRepository = leaveRepository;
    }

    /*
        Fetching the leave request

     */

    @RequestMapping("/leave/{requestId}")
    public ResponseEntity getLeaveDetailsById(@PathVariable("requestId") int requestId)throws LeaveException {
        logger.info("Fetching leave request");
        LeaveRequest leaveRequest = leaveService.getLeaveDetailsById(requestId);
        if(leaveRequest == null)
            throw new LeaveException("Bad fetch");
        else
            return new ResponseEntity(leaveRequest,HttpStatus.OK);
    }

    /*
        Fetching all employee details
    */

    @RequestMapping(value = "/leave",method = RequestMethod.GET)
    public ResponseEntity getLeaveDetails() throws LeaveException{
        logger.info("Fetching All Leave request");
        List<LeaveRequest> leaveRequest = leaveService.getLeaveDetails();
        if(leaveRepository == null)
            throw new LeaveException("Bad fetch");
        else
            return new ResponseEntity(leaveRequest, HttpStatus.OK);
    }

    /*
        Applying for leave request

     */

    @PostMapping("/addLeaveRequest")
    public ResponseEntity addLeaveRequest(@RequestBody LeaveRequest leaveRequest)throws LeaveException {
        String message = leaveService.addLeaveRequest(leaveRequest);
        return new ResponseEntity(message, HttpStatus.OK);
    }


    /*
    Deleting the leave request by Id
 */
    @RequestMapping(value = "leave/{id}",method = RequestMethod.DELETE)
    public ResponseEntity deleteLeaveRequestById(@PathVariable("id") int requestId) throws LeaveException {
        logger.info("Deleting leave request");
        String message = leaveService.deleteLeaveRequestById(requestId);
        return new ResponseEntity(message, HttpStatus.OK);

    }


    /*
    Deleting the All leave request
 */
    @RequestMapping(value = "leave",method = RequestMethod.DELETE)
    public ResponseEntity deleteAllLeaveRequest() throws LeaveException {
        logger.info("Deleting the leave request");
        String message = leaveService.deleteAllLeaveRequest();
        return new ResponseEntity(message, HttpStatus.OK);

    }

    /*
            Find leave request by leave status
     */
    @RequestMapping(value = "/leaveStatus/{leaveStatus}", method = RequestMethod.GET)
    public List<LeaveRequest> getLeaveRequestsByStatus(@PathVariable("leaveStatus") String leaveStatus)throws LeaveException {

        return leaveService.getLeaveRequestsByStatus(LeaveStatus.valueOf(leaveStatus.toUpperCase()));
    }


}
