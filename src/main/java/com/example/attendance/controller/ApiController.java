package com.example.attendance.controller;

import com.example.attendance.dto.AttendanceDTO;
import com.example.attendance.dto.AttendanceDTO3;
import com.example.attendance.dto.EmployeeDTO;
import com.example.attendance.errors.BadRequestAlertException;
import com.example.attendance.model.Account;
import com.example.attendance.model.Attendance;
import com.example.attendance.model.Employee;
import com.example.attendance.service.AccountService;
import com.example.attendance.service.AttendanceService;
import com.example.attendance.service.ControlService;
import com.example.attendance.service.EmployeeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ApiController {
    private final AccountService accountService;
    private final EmployeeService employeeService;
    private final AttendanceService attendanceService;
    private final ControlService controlService;

    public ApiController(AccountService accountService, EmployeeService employeeService, AttendanceService attendanceService, ControlService controlService) {
        this.accountService = accountService;
        this.employeeService = employeeService;
        this.attendanceService = attendanceService;
        this.controlService = controlService;
    }

    /**
     * account_api
     */

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestParam String username, @RequestParam String password) {
        Account authenticatedAccount = accountService.getAccount(username, password);
        if (authenticatedAccount == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(authenticatedAccount);
    }

    /**
     * employee_api
     */

    @GetMapping("/employee")
    public ResponseEntity<List<Employee>> getAllEmployee() {
        try {
            List<Employee> employees = employeeService.getAllEmployees();
            if (employees.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/employee")
    public ResponseEntity<Map<String, Long>> registerEmployee(@RequestBody EmployeeDTO employeeRequest) {
        try {
            Employee employee = employeeService.saveEmployee(employeeRequest);
            Map<String, Long> response = new HashMap<>();
            response.put("id", employee.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long id) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        return employee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * attendance_api
     */

    @PostMapping("/attendance")
    public ResponseEntity<Attendance> saveAttendance(@RequestBody AttendanceDTO attendanceDTO) {
        try {
            Attendance attendance = attendanceService.save(attendanceDTO);
            return new ResponseEntity<>(attendance, HttpStatus.CREATED);
        } catch (BadRequestAlertException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/attendance")
    public ResponseEntity<List<AttendanceDTO3>> getAttendanceByAccountIdAndDate(
            @RequestParam long accountId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate attendanceDate) {

        try {
            List<AttendanceDTO3> attendanceList = attendanceService.getAttendanceByAccountIdAndDate(accountId, Date.valueOf(attendanceDate));
            return ResponseEntity.ok(attendanceList);
        } catch (BadRequestAlertException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * control_api
     */

    @PostMapping("/door")
    public void sendControlMessage(@RequestParam Boolean open) {
        controlService.sendMessageControl(open);
    }

    @GetMapping("/door/status")
    public Boolean getControlStatus() {
        return controlService.getOpenStatus();
    }

    @PostMapping("/door/status")
    public void saveDoorStatus(@RequestParam Boolean open) {
        controlService.saveDoorStatus(open);
    }

    @PostMapping("/mode")
    public void sendModeMessage(@RequestParam Boolean auto) {
        controlService.saveModeControl(auto);
    }

    @GetMapping("/mode/status")
    public Boolean getModeControl() {
        return controlService.getModeControl();
    }
}
