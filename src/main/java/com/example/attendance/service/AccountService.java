package com.example.attendance.service;


import com.example.attendance.model.Account;
import com.example.attendance.repository.AccountRepository;
import org.springframework.stereotype.Service;


@Service
public class AccountService {
	private final AccountRepository accountRepository;

	public AccountService( AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
	}

	public Account createAccount(Account account) {
		return accountRepository.save(account);
	}

	public Account getAccount(String username, String password) {
		return accountRepository.getAccountByUsernameAndPassword(username,password);
	}

	public boolean isUsernameExist(String username) {
		Account account = accountRepository.findByUsername(username);
		return account != null;
	}
}
