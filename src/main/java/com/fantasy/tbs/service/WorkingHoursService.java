package com.fantasy.tbs.service;


import java.time.ZonedDateTime;

/**
 * Service Interface for calculating working hours.
 */
public interface WorkingHoursService {


    /**
     * Calculate an employee's total working hours
     *
     * @param personalNumber the employee's personalNumber
     * @return the employee's total working hours
     */
    public long calculateWorkingHours(String personalNumber);


    /**
     * calculate an employee's total working hours between the startDate and endDate including the startDate and endDate.
     *
     * @param personalNumber the employee's personalNumber
     * @param startDate the startDate of calculation
     * @param endDate the endDate of calculation
     * @return
     */
    public long calculateWorkingHours(String personalNumber, ZonedDateTime startDate, ZonedDateTime endDate);

}
