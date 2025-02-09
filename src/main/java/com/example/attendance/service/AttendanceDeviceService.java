package com.example.attendance.service;

import com.example.attendance.errors.ResourceNotFoundException;
import com.example.attendance.model.AttendanceDevice;
import com.example.attendance.repository.AttendanceDeviceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceDeviceService {
    private final AttendanceDeviceRepository attendanceDeviceRepository;

    public AttendanceDeviceService(AttendanceDeviceRepository attendanceDeviceRepository) {
        this.attendanceDeviceRepository = attendanceDeviceRepository;
    }

    public List<AttendanceDevice> getAllAttendanceDevices() {
        return attendanceDeviceRepository.findAll();
    }

    public AttendanceDevice updateAttendanceDevice(Long id, AttendanceDevice newAttendanceDevice) {
        return attendanceDeviceRepository.findById(id)
                .map(attendanceDevice -> {
                    newAttendanceDevice.setId(id);
                    return attendanceDeviceRepository.save(newAttendanceDevice);
                })
                .orElseThrow(() -> new ResourceNotFoundException("AttendanceDevice not found with id " + id));
    }
}
