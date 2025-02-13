package com.example.attendance.service;

import com.example.attendance.errors.ResourceNotFoundException;
import com.example.attendance.model.MobileDevice;
import com.example.attendance.repository.MobileDeviceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileDeviceService {
    private final MobileDeviceRepository mobileDeviceRepository;

    public MobileDeviceService(MobileDeviceRepository mobileDeviceRepository) {
        this.mobileDeviceRepository = mobileDeviceRepository;
    }

    public List<MobileDevice> getAllMobileDevices() {
        return mobileDeviceRepository.findAll();
    }

    public MobileDevice updateMobileDevice(Long id, MobileDevice newMobileDevice) {
        return mobileDeviceRepository.findById(id)
                .map(mobileDevice -> {
                    newMobileDevice.setId(id);
                    return mobileDeviceRepository.save(newMobileDevice);
                })
                .orElseThrow(() -> new ResourceNotFoundException("MobileDevice not found with id " + id));
    }
}
