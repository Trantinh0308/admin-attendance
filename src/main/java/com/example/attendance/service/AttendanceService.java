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

    public AttendanceService(SimpMessagingTemplate messagingTemplate, AttendanceRepository attendanceRepository, EmployeeService employeeService) {
        this.messagingTemplate = messagingTemplate;
        this.attendanceRepository = attendanceRepository;
        this.employeeService = employeeService;
    }

    public Attendance save(AttendanceDTO attendanceDTO) {
        if (attendanceDTO.getEmployeeId() == null) {
            throw new BadRequestAlertException("Id not exists", ENTITY_NAME, "idexists");
        }
        Employee employee = employeeService.getEmployeeById(attendanceDTO.getEmployeeId())
                .orElseThrow(() -> new BadRequestAlertException("Employee not found", ENTITY_NAME, "employeeNotFound"));

        Attendance attendanceRequest = createAttendance(attendanceDTO, employee);

        if ("camera".equalsIgnoreCase(attendanceDTO.getAttendanceDevice())) {
            sendCameraAttendanceInfo(attendanceDTO, employee);
        }

        return attendanceRequest;
    }

    public List<AttendanceDTO3> getAttendanceByEmployeeAndDate(Long employeeId, Date attendanceDate) {
        return attendanceRepository.findAllAttendanceByEmployeeIdAndDate(employeeId, attendanceDate);
    }

    public List<AttendanceDTO3> getAttendanceByDate(Date attendanceDate) {
        return attendanceRepository.findAllAttendanceByDate(attendanceDate);
    }

    private Attendance createAttendance(AttendanceDTO attendanceDTO, Employee employee) {
        Attendance attendance = new Attendance();
        attendance.setEmployee(employee);
        attendance.setImageCode(attendanceDTO.getImageCode());
        attendance.setDate(attendanceDTO.getDate());
        attendance.setTime(attendanceDTO.getTime());

        if ("camera".equalsIgnoreCase(attendanceDTO.getAttendanceDevice())) {
            attendance.setDeviceName("Camera");
        } else {
            attendance.setDeviceName("Mobile");
        }

        String status = attendanceRepository.existsByEmployee_IdAndDate(attendanceDTO.getEmployeeId(), attendanceDTO.getDate())
                ? "Check out" : "Check in";
        attendance.setStatus(status);

        return attendanceRepository.save(attendance);
    }

    private void sendCameraAttendanceInfo(AttendanceDTO attendanceDTO, Employee employee) {
        AttendanceDTO2 attendanceDTO2 = new AttendanceDTO2();
        attendanceDTO2.setEmployeeName(employee.getFullName());
        attendanceDTO2.setImageCode(attendanceDTO.getImageCode());
        attendanceDTO2.setDepartment(employee.getDepartment());
        attendanceDTO2.setTime(attendanceDTO.getDate() + " " + attendanceDTO.getTime());
        messagingTemplate.convertAndSend("/topic/data", attendanceDTO2);
    }
}
