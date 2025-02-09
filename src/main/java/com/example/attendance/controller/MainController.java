package com.example.attendance.controller;

import com.example.attendance.config.Constants;
import com.example.attendance.dto.AttendanceDTO3;
import com.example.attendance.model.Account;
import com.example.attendance.model.Timekeeping;
import com.example.attendance.model.WorkCalendar;
import com.example.attendance.service.AccountService;
import com.example.attendance.service.AttendanceService;
import com.example.attendance.service.CalendarService;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;

import java.util.List;

@Controller
public class MainController {
    private final AccountService accountService;
    private final AttendanceService attendanceService;
    private final CalendarService calendarService;

    public MainController(AccountService accountService, AttendanceService attendanceService, CalendarService calendarService) {
        this.accountService = accountService;
        this.attendanceService = attendanceService;
        this.calendarService = calendarService;
    }
    @GetMapping("/login")
    public String showLoginForm() {
        return Constants.PAGE_LOGIN;
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,HttpSession session,
                          @RequestParam("password") String password, Model model) {
        Account account = accountService.getAccount(username, password);
        if (account == null || account.getRole() != 1) {
            model.addAttribute(Constants.ERROR, Constants.ERROR_LOGIN);
            return Constants.PAGE_LOGIN;
        }
        session.setAttribute(Constants.USERNAME, account.getUsername());
        return Constants.REDIRECT_ATTENDANCE;
    }

    @GetMapping("/registerFaceId")
    public String formRegisterFaceId() {
        return "registerFaceId";
    }

    @GetMapping("/register")
    public String registerForm() {
        return Constants.PAGE_LOGIN;
    }

    @PostMapping("/register")
    public String registerAccount(@RequestParam("username") String username,
                                  @RequestParam("password") String password,
                                  @RequestParam("confirmPassword") String confirmPassword,
                                  Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute(Constants.ERROR, Constants.ERROR_CONFIRM_PASS);
            return Constants.PAGE_LOGIN;
        }
        if (accountService.isUsernameExist(username)) {
            model.addAttribute(Constants.ERROR, Constants.ERROR_CONFIRM_USERNAME);
            return Constants.PAGE_LOGIN;
        }

        Account newAccount = new Account();
        newAccount.setUsername(username);
        newAccount.setPassword(password);
        accountService.createAccount(newAccount);
        model.addAttribute(Constants.ERROR, Constants.CONFIRM_REGISTER_SUCCESS);
        return Constants.PAGE_LOGIN;
    }

    @GetMapping("/attendance")
    public String manageAttendance() {
        return "/attendance";
    }

    @GetMapping(value = "/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(Constants.USERNAME);
        return Constants.REDIRECT_LOGIN;
    }

    @PostMapping("/history")
    public String historyByDate(@RequestParam(value = "attendanceDate") @DateTimeFormat(pattern = "yyyy-MM-dd")
                                    LocalDate attendanceDate,Model model) {
        List<AttendanceDTO3> histories = attendanceService.getAttendanceByDate(Date.valueOf(attendanceDate));
        model.addAttribute("listH", histories);
        model.addAttribute("attendanceDate", attendanceDate);
        return "history";
    }

    @GetMapping("/history")
    public String currentDateHistory(Model model) {
        LocalDate localDate = LocalDate.now();
        Date currentDate = Date.valueOf(localDate);
        List<AttendanceDTO3> histories = attendanceService.getAttendanceByDate(currentDate);
        model.addAttribute("listH", histories);
        model.addAttribute("attendanceDate", currentDate);
        return "history";
    }

    @GetMapping("/timekeeping")
    public String getTimekeeping(Model model) {
        List<WorkCalendar> calendarList = calendarService.getWorkCalendarForCurrentMonth();
        List<Timekeeping> timekeepingList = calendarService.getAllTimekeepingByCurrentMonth();
        model.addAttribute("listC", calendarList);
        model.addAttribute("timekeepingList", timekeepingList);
        return "timekeeping";
    }

    @GetMapping("/findTimekeeping")
    public String getTimekeepingByMonthYear(@RequestParam("month") int month,
                                            @RequestParam("year") int year, Model model) {
        List<WorkCalendar> calendarList = calendarService.getWorkCalendarForMonthYear(month, year);
        List<Timekeeping> timekeepingList = calendarService.getAllTimekeepingByMonthAndYear(month,year);
        model.addAttribute("listC", calendarList);
        model.addAttribute("timekeepingList", timekeepingList);
        model.addAttribute("month", month);
        model.addAttribute("year", String.valueOf(year));
        return "timekeeping";
    }
}
