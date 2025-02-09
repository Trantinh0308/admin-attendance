package com.example.attendance.controller;

import com.example.attendance.dto.AttendanceDTO;
import com.example.attendance.dto.EmployeeDTO;
import com.example.attendance.errors.BadRequestAlertException;
import com.example.attendance.model.Attendance;
import com.example.attendance.model.Employee;
import com.example.attendance.service.AttendanceService;
import com.example.attendance.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ApiController {
    private final EmployeeService employeeService;
    private final AttendanceService attendanceService;
    public ApiController(EmployeeService employeeService, AttendanceService attendanceService) {
        this.employeeService = employeeService;
        this.attendanceService = attendanceService;
    }

    /**
     * employee_api
     */

    @GetMapping("/employee")
    public ResponseEntity<?> getAllEmployee() {
        try {
            List<Employee> employees = employeeService.getAllEmployees();
            if (employees.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error");
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
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
