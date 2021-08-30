package com.fantasy.tbs.service.impl;

import com.fantasy.tbs.domain.TimeBooking;
import com.fantasy.tbs.service.TimeBookingService;
import com.fantasy.tbs.service.WorkingHoursService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service Implementation for calculating working hours.
 */
@Service
public class WorkingHoursServiceImpl implements WorkingHoursService {

    private final Logger log = LoggerFactory.getLogger(WorkingHoursServiceImpl.class);

    private final TimeBookingService timeBookingService;

    public WorkingHoursServiceImpl(TimeBookingService timeBookingService) {
        this.timeBookingService = timeBookingService;
    }

    @Override
    public long calculateWorkingHours(String personalNumber) {
        log.debug("Request to calculate working hours by personNumber : {}", personalNumber);
        // Find the timeBookings by personalNumber
        List<ZonedDateTime> bookingList = timeBookingService.findByPersonalNumber(personalNumber).stream().map(e -> e.getBooking()).collect(Collectors.toList());

        // Group the timeBookings by date(yyyyMMdd)
        Map<String, List<ZonedDateTime>> bookingListMap = bookingList.stream().collect(Collectors.groupingBy(booking -> {
            return DateTimeFormatter.ofPattern("yyyyMMdd").format(booking);
        }));

        // Calculate the working minutes of each day
        Map<String, Integer> map = new ConcurrentHashMap<>();
        bookingListMap.forEach((date, bookings) -> {
            // Only if the number of the ZoneDateTime is greater than 2 each day, the working hours can be calculated
            if (bookings.size() > 1) {
                ZonedDateTime startTime = this.getMinDateTime(bookings);
                ZonedDateTime endTime = this.getMaxDateTime(bookings);
                int minutes = (int) ChronoUnit.MINUTES.between(startTime, endTime);
                map.put(date, minutes);
            }
        });
        // Sum up the working minutes and give the working hours
        int hours = map.values().stream().mapToInt(e -> e).sum() / 60;
        return hours;
    }

    private ZonedDateTime getMaxDateTime(List<ZonedDateTime> zonedDateTimeList) {
        return zonedDateTimeList.stream().max((o1, o2) -> {
            return o1.compareTo(o2);
        }).orElse(ZonedDateTime.now());
    }

    private ZonedDateTime getMinDateTime(List<ZonedDateTime> zonedDateTimeList) {
        return zonedDateTimeList.stream().min((o1, o2) -> {
            return o2.compareTo(o1);
        }).orElse(ZonedDateTime.now());
    }

    @Override
    public long calculateWorkingHours(String personalNumber, ZonedDateTime startDate, ZonedDateTime endDate) {
        // TODO Calculate an employee's working hours of a period
        return 0;
    }

}
