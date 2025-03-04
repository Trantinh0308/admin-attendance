package com.example.attendance.repository;

import com.example.attendance.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Boolean existsEmployeeById(Long id);

    Boolean existsEmployeeByPhoneNumber(String phoneNumber);

    @Query("SELECT e.id FROM Employee e WHERE e.account.id = :accountId")
    Optional<Long> findEmployeeIdByAccountId(@Param("accountId") Long accountId);
}
