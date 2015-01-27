package com.dynastiesofscorpia.jsp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Time extends SimpleTagSupport {
    private int time;

    public void setTime(int time) {
        this.time = time;
    }

    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();

        int day = (int) TimeUnit.SECONDS.toDays(time);
        long hours = TimeUnit.SECONDS.toHours(time) - TimeUnit.DAYS.toHours(day);
        long minute = TimeUnit.SECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(time));

        String timeDescription = "";
        if(day > 0)
            timeDescription += day + "T ";
        if(hours > 0)
            timeDescription += hours + "h ";
        if(minute > 0)
            timeDescription += minute + "min ";

        if(day == 0 && hours == 0 && minute == 0)
            timeDescription = "unter 1min ";

        out.print(timeDescription);
    }
}
