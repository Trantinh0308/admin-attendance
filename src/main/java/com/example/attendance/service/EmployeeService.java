package com.example.attendance.service;

import com.example.attendance.dto.EmployeeDTO;
import com.example.attendance.model.Account;
import com.example.attendance.model.Employee;
import com.example.attendance.repository.AccountRepository;
import com.example.attendance.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final AccountRepository accountRepository;

    public EmployeeService(EmployeeRepository employeeRepository, AccountRepository accountRepository) {
        this.employeeRepository = employeeRepository;
        this.accountRepository = accountRepository;
    }
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }
    public Optional<Employee> getEmployeeById(long id){
        return employeeRepository.findById(id);
    }
    public Employee saveEmployee(EmployeeDTO employeeDTO){
        Account account = new Account();
        account.setUsername(employeeDTO.getPhoneNumber());
        account.setPassword("12345");
        account.setRole(2);
        accountRepository.save(account);

        Employee employee = new Employee();
        employee.setAccount(account);
        employee.setFullName(employeeDTO.getFullName());
        employee.setGender(employeeDTO.getGender());
        employee.setPhoneNumber(employeeDTO.getPhoneNumber());
        employee.setDepartment(employeeDTO.getDepartment());
        return employeeRepository.save(employee);
    }
}
