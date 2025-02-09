package com.example.attendance.repository;

import com.example.attendance.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	Account getAccountByUsernameAndPassword(String username,String password);
	Account findByUsername(String username);
}
