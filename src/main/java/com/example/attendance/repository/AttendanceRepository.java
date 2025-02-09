package com.example.attendance.repository;

import com.example.attendance.dto.AttendanceDTO3;
import com.example.attendance.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    boolean existsByEmployee_IdAndDate(Long employeeId, Date date);

    @Query("SELECT new com.example.attendance.dto.AttendanceDTO3(a.imageCode,a.employee.id, a.employee.fullName, a.employee.department, a.date, a.time, a.status, a.deviceName) " +
            "FROM Attendance a " +
            "WHERE a.employee.id = :employeeId AND a.date = :attendanceDate")
    List<AttendanceDTO3> findAllAttendanceByEmployeeIdAndDate(@Param("employeeId") Long employeeId, @Param("attendanceDate") Date attendanceDate);

    @Query("SELECT new com.example.attendance.dto.AttendanceDTO3(a.imageCode,a.employee.id, a.employee.fullName, a.employee.department, a.date, a.time, a.status, a.deviceName) " +
            "FROM Attendance a " +
            "WHERE a.date = :attendanceDate")
    List<AttendanceDTO3> findAllAttendanceByDate(@Param("attendanceDate") Date attendanceDate);

    @Query("SELECT a FROM Attendance a WHERE a.date = :date AND a.status = 'Check in' AND a.employee.id = :employeeId ORDER BY a.id ASC")
    Attendance findFirstCheckInByDateAndEmployee(@Param("date") Date date, @Param("employeeId") Long employeeId);

    @Query("SELECT a FROM Attendance a WHERE a.date = :date AND a.status = 'Check out' AND a.employee.id = :employeeId ORDER BY a.id ASC")
    List<Attendance> findFirstCheckOutByDateAndEmployee(@Param("date") Date date, @Param("employeeId") Long employeeId);
}
