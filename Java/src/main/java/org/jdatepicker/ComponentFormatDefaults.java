package org.jdatepicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by jheyns on 2014/10/16.
 */
public class ComponentFormatDefaults {

    private DateFormat todayDateFormat;
    private DateFormat selectedDateFormat;

    public ComponentFormatDefaults() {
//        todayDateFormat = getMediumDateFormat();
//        selectedDateFormat = getMediumDateFormat();
        
        todayDateFormat = getDayMonthYearDateFormat();
        selectedDateFormat = getDayMonthYearDateFormat();
    }

    private DateFormat getMediumDateFormat() {
        return SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM);
    }

    private DateFormat getDayMonthYearDateFormat() {
        String datePattern = "dd/MM/yyyy";
        return new SimpleDateFormat(datePattern);
    }

    public DateFormat getSelectedDateFormat() {
        return selectedDateFormat;
    }

    public void setSelectedDateFormat(DateFormat selectedDateFormat) {
        this.selectedDateFormat = selectedDateFormat;
    }

    public DateFormat getTodayDateFormat() {
        return todayDateFormat;
    }

    public void setTodayDateFormat(DateFormat todayDateFormat) {
        this.todayDateFormat = todayDateFormat;
    }

}
