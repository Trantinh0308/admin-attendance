package com.example.attendance.service;

import com.example.attendance.dto.EmployeeDTO;
import com.example.attendance.errors.BadRequestAlertException;
import com.example.attendance.model.Account;
import com.example.attendance.model.Employee;
import com.example.attendance.repository.AccountRepository;
import com.example.attendance.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

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

    public Boolean checkExistsEmployeeById(long id){
        return employeeRepository.existsEmployeeById(id);
    }

    public Optional<Long> getEmployeeIdByAccountId(long accountId){
        return employeeRepository.findEmployeeIdByAccountId(accountId);
    }

    public Employee saveEmployee(EmployeeDTO employeeDTO){
        if (Boolean.TRUE.equals(employeeRepository.existsEmployeeByPhoneNumber(employeeDTO.getPhoneNumber()))){
            throw new BadRequestAlertException("PhoneNumber exists",ENTITY_NAME,"PhoneNumber exists");
        }
        Account account = new Account();
        account.setUsername(employeeDTO.getPhoneNumber());
        account.setPassword(Utils.generatePassword(8));
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
