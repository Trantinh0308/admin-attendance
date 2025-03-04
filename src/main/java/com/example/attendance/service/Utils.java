package com.example.attendance.service;

import com.example.attendance.errors.BadRequestAlertException;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Random;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

public class Utils {

    private static final Random random = new Random();

    public static String getAttendanceStatusByQuantity(long quantity) {
        LocalTime currentTime = LocalTime.now();
        if (quantity == 0) return "Check In";
        else if (quantity == 1 && currentTime.isAfter(LocalTime.of(17, 0))) return "Check Out";
        else throw new BadRequestAlertException("Attendance limit exceeded for this time", ENTITY_NAME, "Attendance limit exceeded for this time");
    }

    public static Date createSqlDate(int day,int month,int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return new Date(calendar.getTimeInMillis());
    }

    public static double calculatorWorkingInDay(Time startTime, Time endTime) {
        LocalTime startLocalTime = startTime.toLocalTime();
        LocalTime endLocalTime = endTime.toLocalTime();
        long hours = ChronoUnit.HOURS.between(startLocalTime, endLocalTime);
        long minutes = ChronoUnit.MINUTES.between(startLocalTime, endLocalTime) % 60;
        double totalHours = hours + minutes / 60.0;
        if (totalHours < 4) {
            return 0.0;
        } else if (totalHours < 8) {
            return 0.5;
        } else {
            return 1.0;
        }
    }

    public static String getShortDayOfWeek(int dayOfWeek) {
        return switch (dayOfWeek) {
            case 1 -> "T2";
            case 2 -> "T3";
            case 3 -> "T4";
            case 4 -> "T5";
            case 5 -> "T6";
            case 6 -> "T7";
            case 7 -> "CN";
            default -> "";
        };
    }

    public static String generatePassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }
}
