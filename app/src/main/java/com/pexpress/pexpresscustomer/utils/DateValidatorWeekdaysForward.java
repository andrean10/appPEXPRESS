package com.pexpress.pexpresscustomer.utils;

import android.os.Parcel;

import com.google.android.material.datepicker.CalendarConstraints;

import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

public class DateValidatorWeekdaysForward implements CalendarConstraints.DateValidator {

    public static final Creator<DateValidatorWeekdaysForward> CREATOR =
            new Creator<DateValidatorWeekdaysForward>() {
                @Override
                public DateValidatorWeekdaysForward createFromParcel(Parcel source) {
                    return new DateValidatorWeekdaysForward(source.readLong());
                }

                @Override
                public DateValidatorWeekdaysForward[] newArray(int size) {
                    return new DateValidatorWeekdaysForward[size];
                }
            };
    private final Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"));
    private final long point;

    private DateValidatorWeekdaysForward(long point) {
        this.point = point;
    }

    public static DateValidatorWeekdaysForward from(long point) {
        return new DateValidatorWeekdaysForward(point);
    }

    @Override
    public boolean isValid(long date) {
        utc.setTimeInMillis(date);
        int year = utc.get(Calendar.YEAR);
        int month = utc.get(Calendar.MONTH);
        int dayOfWeek = utc.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY && date >= point;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return o instanceof DateValidatorWeekdaysForward;
    }

    @Override
    public int hashCode() {
        Object[] hashedFields = {};
        return Arrays.hashCode(hashedFields);
    }
}
