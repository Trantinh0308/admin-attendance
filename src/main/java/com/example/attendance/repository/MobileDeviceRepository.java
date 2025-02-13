package com.example.attendance.repository;

import com.example.attendance.model.MobileDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MobileDeviceRepository extends JpaRepository<MobileDevice,Long> {
    @Query("SELECT m.mobileId FROM MobileDevice m JOIN m.account a WHERE a.username = :username AND a.password = :password")
    Optional<String> findMobileIdByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}
