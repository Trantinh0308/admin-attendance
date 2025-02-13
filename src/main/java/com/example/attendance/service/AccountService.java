package com.example.attendance.service;


import com.example.attendance.dto.UserDTO;
import com.example.attendance.errors.InvalidMobileIdException;
import com.example.attendance.model.Account;
import com.example.attendance.model.MobileDevice;
import com.example.attendance.repository.AccountRepository;
import com.example.attendance.repository.MobileDeviceRepository;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;

@Service
public class AccountService {
	private final AccountRepository accountRepository;
	private final MobileDeviceRepository mobileDeviceRepository;

	public AccountService(AccountRepository accountRepository, MobileDeviceRepository mobileDeviceRepository) {
        this.accountRepository = accountRepository;
        this.mobileDeviceRepository = mobileDeviceRepository;
    }

	public void createAccount(Account account) {
		accountRepository.save(account);
	}

	public Account loginAdminAccount(String username, String password) {
		return accountRepository.findAccountByUsernameAndPassword(username,password);
	}

	public Account loginUserAccount(UserDTO userDTO) throws AccountNotFoundException, InvalidMobileIdException {
		Account account = accountRepository.findAccountByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword());
		if (account == null) {
			throw new AccountNotFoundException("Account not found for username: " + userDTO.getUsername());
		}

		Optional<String> mobileIdOptional = mobileDeviceRepository.findMobileIdByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword());
		if (mobileIdOptional.isPresent()) {
			String currentMobileId = mobileIdOptional.get();
			if (!currentMobileId.equals(userDTO.getMobileId())) {
				throw new InvalidMobileIdException("Invalid mobileId for username: " + userDTO.getUsername());
			}
		}
		else {
			MobileDevice mobileDevice = new MobileDevice();
			mobileDevice.setAccount(account);
			mobileDevice.setMobileId(userDTO.getMobileId());
			mobileDeviceRepository.save(mobileDevice);
		}
		return account;
	}

	public boolean isUsernameExist(String username) {
		return accountRepository.existsAccountByUsername(username);
	}
}
