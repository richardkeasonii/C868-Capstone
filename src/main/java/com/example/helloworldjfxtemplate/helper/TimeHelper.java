package com.example.helloworldjfxtemplate.helper;

import com.example.helloworldjfxtemplate.MainApplication;

import java.time.*;

/**
 * A Time Helper class to supply methods for other functions to handle time conversions.
 */
public class TimeHelper {
    /**
     * Converts a given UTC time to the Local time.
     * @param utcDateTime A given UTC time, such as from the database.
     * @return The UTC time represented in the local timezone.
     */
    public static ZonedDateTime convertUTCToLocal(LocalDateTime utcDateTime) {
        ZonedDateTime utcZonedDateTime = utcDateTime.atZone(ZoneId.of("UTC"));
        return utcZonedDateTime.withZoneSameInstant(MainApplication.zoneId);
    }

//    public static ZonedDateTime convertLocalToUTC(LocalDateTime localDateTime) {
//        ZonedDateTime localZonedDateTime = localDateTime.atZone(MainApplication.zoneId);
//        return localZonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
//    }

    /**
     * Converts the current time to a UTC time.
     * @return The current time in UTC.
     */
    public static LocalDateTime convertNowToUTC(){
        // Get the current time
        LocalDateTime now = LocalDateTime.now();

        // Convert to UTC
        LocalDateTime utcTime = now.atOffset(ZoneOffset.UTC).toLocalDateTime();


        return utcTime;
    }

    /**
     * Checks if a given time is within the ET office hours.
     * @param givenTime Time to check if it falls within office hours.
     * @return True if the time is within office hours, false otherwise.
     */
    public static boolean isInOfficeHours(LocalDateTime givenTime) {
        ZoneId easternZoneId = ZoneId.of("America/New_York");

        ZonedDateTime easternDateTime = givenTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(easternZoneId);
        LocalTime easternTime = easternDateTime.toLocalTime();

        LocalTime startTime = LocalTime.of(8, 0);  // 8:00 a.m.
        LocalTime endTime = LocalTime.of(22, 0);   // 10:00 p.m.

        System.out.println(easternTime);
        System.out.println(startTime);
        System.out.println(endTime);


        return (easternTime.isAfter(startTime) && easternTime.isBefore(endTime));
    }


}
