package com.example.attendance.service;

import com.example.attendance.dto.AttendanceDTO;
import com.example.attendance.dto.AttendanceDTO2;
import com.example.attendance.dto.AttendanceDTO3;
import com.example.attendance.errors.BadRequestAlertException;
import com.example.attendance.model.Attendance;
import com.example.attendance.model.Employee;
import com.example.attendance.repository.AttendanceRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.util.List;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

@Service
public class AttendanceService {
    private final SimpMessagingTemplate messagingTemplate;
    private final AttendanceRepository attendanceRepository;
    private final EmployeeService employeeService;

    public AttendanceService(SimpMessagingTemplate messagingTemplate, AttendanceRepository attendanceRepository,
                             EmployeeService employeeService) {
        this.messagingTemplate = messagingTemplate;
        this.attendanceRepository = attendanceRepository;
        this.employeeService = employeeService;
    }

    public Attendance save(AttendanceDTO attendanceDTO) {
        Employee employee = getEmployee(attendanceDTO.getEmployeeId());
        long attendanceQuantity = getNumberAttendanceInToday(attendanceDTO.getEmployeeId());

        String attendanceStatus = Utils.getAttendanceStatusByQuantity(attendanceQuantity);
        Attendance attendanceRequest = createAttendance(attendanceDTO, employee, attendanceStatus);
        if ("camera".equalsIgnoreCase(attendanceDTO.getAttendanceDevice())) {
            sendAttendanceInfo(attendanceDTO, employee);
        }

        return attendanceRequest;
    }

    public long getNumberAttendanceInToday(long employeeId){
        if (Boolean.FALSE.equals(employeeService.checkExistsEmployeeById(employeeId))) throw new BadRequestAlertException("Employee not found", ENTITY_NAME, "employeeNotFound");
        return attendanceRepository.countByDateAndEmployeeId(employeeId);
    }

    public List<AttendanceDTO3> getAttendanceByAccountIdAndDate(long accountId, Date attendanceDate) {
        Long employeeId = getEmployeeIdByAccount(accountId);
        return attendanceRepository.findAllAttendanceByEmployeeIdAndDate(employeeId, attendanceDate);
    }

    public List<AttendanceDTO3> getAttendanceByDate(Date attendanceDate) {
        return attendanceRepository.findAllAttendanceByDate(attendanceDate);
    }

    private Attendance createAttendance(AttendanceDTO attendanceDTO, Employee employee, String status) {
        Attendance attendance = new Attendance();
        attendance.setEmployee(employee);
        attendance.setImageCode(attendanceDTO.getImageCode());
        attendance.setDate(attendanceDTO.getDate());
        attendance.setTime(attendanceDTO.getTime());
        attendance.setDeviceName(getDeviceName(attendanceDTO.getAttendanceDevice()));
        attendance.setStatus(status);

        return attendanceRepository.save(attendance);
    }

    private String getDeviceName(String attendanceDevice) {
        return "camera".equalsIgnoreCase(attendanceDevice) ? "Camera" : "Mobile";
    }

    private Employee getEmployee(Long employeeId) {
        return employeeService.getEmployeeById(employeeId)
                .orElseThrow(() -> new BadRequestAlertException("Employee not found", ENTITY_NAME, "employeeNotFound"));
    }

    private Long getEmployeeIdByAccount(long accountId) {
        return employeeService.getEmployeeIdByAccountId(accountId)
                .orElseThrow(() -> new BadRequestAlertException("Id not found", ENTITY_NAME, "idNotFound"));
    }

    private void sendAttendanceInfo(AttendanceDTO attendanceDTO, Employee employee) {
        AttendanceDTO2 attendanceDTO2 = new AttendanceDTO2();
        attendanceDTO2.setEmployeeName(employee.getFullName());
        attendanceDTO2.setImageCode(attendanceDTO.getImageCode());
        attendanceDTO2.setDepartment(employee.getDepartment());
        attendanceDTO2.setTime(attendanceDTO.getDate() + " " + attendanceDTO.getTime());
        messagingTemplate.convertAndSend("/topic/data", attendanceDTO2);
    }
}