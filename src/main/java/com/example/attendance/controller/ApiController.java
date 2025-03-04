package com.example.attendance.controller;

import com.example.attendance.config.Constants;
import com.example.attendance.dto.AttendanceDTO;
import com.example.attendance.dto.AttendanceDTO3;
import com.example.attendance.dto.EmployeeDTO;
import com.example.attendance.dto.UserDTO;
import com.example.attendance.errors.BadRequestAlertException;
import com.example.attendance.errors.ErrorResponse;
import com.example.attendance.errors.InvalidMobileIdException;
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
import org.springframework.web.client.RestClient;

import javax.security.auth.login.AccountNotFoundException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.System.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ApiController {
    private final AccountService accountService;
    private final EmployeeService employeeService;
    private final AttendanceService attendanceService;
    private final ControlService controlService;
    private final RestClient restClient;

    public ApiController(AccountService accountService, EmployeeService employeeService,
                         AttendanceService attendanceService, ControlService controlService, RestClient.Builder restClientBuilder) {
        this.accountService = accountService;
        this.employeeService = employeeService;
        this.attendanceService = attendanceService;
        this.controlService = controlService;
        this.restClient = restClientBuilder.baseUrl(Constants.URL_SERVO).build();
    }

    /**
     * account_api
     */

    @PostMapping("/user/login")
    public ResponseEntity<Object> getUserAccount(@RequestBody UserDTO userDTO) {
        try {
            Account account = accountService.loginUserAccount(userDTO);
            return ResponseEntity.ok(account);
        } catch (AccountNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (InvalidMobileIdException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * employee_api
     */

    @GetMapping("/employee")
    public ResponseEntity<Object> getAllEmployee() {
        try {
            List<Employee> employees = employeeService.getAllEmployees();
            if (employees.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/employee")
    public ResponseEntity<Object> registerEmployee(@RequestBody EmployeeDTO employeeRequest) {
        try {
            Employee employee = employeeService.saveEmployee(employeeRequest);
            Map<String, Long> response = new HashMap<>();
            response.put("id", employee.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (BadRequestAlertException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long id) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        return employee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/employeeId")
    public ResponseEntity<Long> getEmployeeByAccountId(@RequestParam("accountId") Long accountId) {
        Optional<Long> employee = employeeService.getEmployeeIdByAccountId(accountId);
        return employee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * attendance_api
     */

    @PostMapping("/attendance")
    public ResponseEntity<Object> saveAttendance(@RequestBody AttendanceDTO attendanceDTO) {
        try {
            Attendance attendance = attendanceService.save(attendanceDTO);
            return new ResponseEntity<>(attendance, HttpStatus.CREATED);

        } catch (BadRequestAlertException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/attendance")
    public ResponseEntity<Object> getAttendanceByAccountIdAndDate(
            @RequestParam long accountId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate attendanceDate) {

        try {
            List<AttendanceDTO3> attendanceList = attendanceService.getAttendanceByAccountIdAndDate(accountId, Date.valueOf(attendanceDate));
            return ResponseEntity.ok(attendanceList);
        } catch (BadRequestAlertException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/attendance/today")
    public ResponseEntity<Object> numberAttendanceInToday(@RequestParam Long employeeId) {
        try {
            Map<String, Long> response = new HashMap<>();
            response.put("number", attendanceService.getNumberAttendanceInToday(employeeId));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (BadRequestAlertException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
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
        String endpoint = open ? "/on" : "/off";
        try {
            restClient.get()
                    .uri(endpoint)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            err.println("Error sending command to ESP32: " + e.getMessage());
        }
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
