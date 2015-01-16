

import static java.time.DayOfWeek.*;
import static java.time.temporal.TemporalAdjusters.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import org.junit.Test;

public class DateTimeExample {

    @Test
    public void testName() throws Exception {
        LocalDate date = LocalDate.now();
        System.out.println(date);
        LocalDate date2 = date.plusYears(3);
        System.out.println(date2);

        System.out.println(LocalDate.of(2014, 7, 11).isBefore(date));
        System.out.println(LocalDate.of(2014, 7, 11).isAfter(date));

        LocalDate date3 = date.with(lastDayOfMonth());
        System.out.println(date3);

        LocalDate date4 = date.with(next(MONDAY));
        System.out.println(date4);

        Period period = Period.between(date, date2);
        System.out.println(period);

        long between = ChronoUnit.DAYS.between(date, date2);
        System.out.println(between);
    }
}
