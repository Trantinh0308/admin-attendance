package com.example.attendance.service;

import com.example.attendance.model.Attendance;
import com.example.attendance.model.Employee;
import com.example.attendance.model.Timekeeping;
import com.example.attendance.model.WorkCalendar;
import com.example.attendance.repository.AttendanceRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarService {
    private static final Logger log = LogManager.getLogger(CalendarService.class);
    private final EmployeeService employeeService;
    private final AttendanceRepository attendanceRepository;

    public CalendarService(EmployeeService employeeService, AttendanceRepository attendanceRepository) {
        this.employeeService = employeeService;
        this.attendanceRepository = attendanceRepository;
    }

    public List<WorkCalendar> getWorkCalendarForCurrentMonth() {
        List<WorkCalendar> list = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        YearMonth currentMonth = YearMonth.from(currentDate);

        for (int day = 1; day <= currentMonth.lengthOfMonth(); day++) {
            LocalDate date = LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), day);
            String dayOfWeek = Utils.getShortDayOfWeek(date.getDayOfWeek().getValue());
            list.add(new WorkCalendar(String.format("%02d", day), dayOfWeek));
        }

        return list;
    }

    public List<WorkCalendar> getWorkCalendarForMonthYear(int month, int year) {
        List<WorkCalendar> list = new ArrayList<>();
        YearMonth yearMonth = YearMonth.of(year, month);
        int lengthOfMonth = yearMonth.lengthOfMonth();

        for (int day = 1; day <= lengthOfMonth; day++) {
            LocalDate date = LocalDate.of(year, month, day);
            String dayOfWeek = Utils.getShortDayOfWeek(date.getDayOfWeek().getValue());
            list.add(new WorkCalendar(String.format("%02d", day), dayOfWeek));
        }

        return list;
    }

    public Timekeeping employeeTimeKeepingByIdAndMonthAndYear(long employeeId, int month, int year){
        Timekeeping timekeeping = new Timekeeping();
        List<Double> listWorkTime = new ArrayList<>();

        YearMonth yearMonth = YearMonth.of(year, month);
        int lengthOfMonth = yearMonth.lengthOfMonth();
        for (int day = 1; day <= lengthOfMonth; day++) {
            Date dateSql = Utils.createSqlDate(day,month,year);
            Attendance attendanceCheckIn = attendanceRepository.findFirstCheckInByDateAndEmployee(dateSql,employeeId);
            List<Attendance> attendanceCheckOut = attendanceRepository.findFirstCheckOutByDateAndEmployee(dateSql,employeeId);

            if (attendanceCheckIn == null || attendanceCheckOut.isEmpty()){
                listWorkTime.add(0.0);
                continue;
            }
            Time timeCheckIn = attendanceCheckIn.getTime();
            Time timeCheckOut = attendanceCheckOut.get(0).getTime();
            Double timeWork = Utils.calculatorWorkingInDay(timeCheckIn,timeCheckOut);
            listWorkTime.add(timeWork);
        }
        Optional<Employee> optional = employeeService.getEmployeeById(employeeId);
        optional.ifPresent(timekeeping::setEmployee);
        timekeeping.setSum(listWorkTime);
        timekeeping.setListTimekeeping(listWorkTime);
        return timekeeping;
    }

    public List<Timekeeping> getAllTimekeepingByMonthAndYear(int month, int year){
        List<Timekeeping> timekeepingList = new ArrayList<>();
        List<Employee> employees = employeeService.getAllEmployees();
        for(Employee employee : employees){
            Timekeeping timekeeping = employeeTimeKeepingByIdAndMonthAndYear(employee.getId(),month,year);
            timekeepingList.add(timekeeping);
        }
        return  timekeepingList;
    }

    public List<Timekeeping> getAllTimekeepingByCurrentMonth(){
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();
        return getAllTimekeepingByMonthAndYear(currentMonth,currentYear);
    }
}
