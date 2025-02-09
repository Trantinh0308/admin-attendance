package com.example.attendance.repository;

import com.example.attendance.model.AttendanceDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceDeviceRepository extends JpaRepository<AttendanceDevice,Long> {

}
