package org.example.Util;

import lombok.extern.slf4j.Slf4j;
import org.example.Exception.InvalidInputException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class DateTimeValidationUtils {

    public static boolean validateDate(String date) {
        if (date == null || date.trim().equals(""))
        {
            return false;
        } else
        {
            SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd");
            sdfrmt.setLenient(false);
            try
            {
                sdfrmt.parse(date);
            } catch (ParseException e)
            {
                log.error("invalid date {}",date);
                return false;
            }
        }
        return true;
    }

    public static boolean validateTime(String time) {
        String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
        Pattern p = Pattern.compile(regex);
        if (time == null) {
            return false;
        }
        Matcher m = p.matcher(time);
        if(!m.matches()) log.error("Invalid Time {}",time);
        return m.matches();
    }

    public static void validateStartAndEndDate(String startDate, String startTime, String endDate, String endTime) {
        if( !(validateTime(startTime) && validateTime(endTime) && validateDate(startDate) && validateDate(endDate)) ) {
            throw new InvalidInputException("Invalid Input. Required Date Format dd-mm-yyyy. Required Time Format hh:mm in 24 hr");
        }
        try {
            Date startDateTime = toDate(startDate, startTime);
            Date endDateTime = toDate(endDate, endTime);
            Date currentTime = Calendar.getInstance().getTime();
            if (startDateTime.before(currentTime) || endDateTime.before(startDateTime)) {
                throw new InvalidInputException("endTime should be greater than start time");
            }
        } catch (ParseException e) {
            throw new InvalidInputException("Invalid Input. Required Date Format dd-mm-yyyy. Required Time Format hh:mm in 24 hr");
        }
    }

    public static Date toDate(String date, String timeIn24Hr) throws ParseException {
        String value = date + " " + timeIn24Hr;
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(value);
    }
}
