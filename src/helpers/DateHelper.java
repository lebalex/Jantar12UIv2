/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author ivc_LebedevAV
 */
public class DateHelper {
 public static String CurrentDate(String format) {
        String date_str = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sd = new SimpleDateFormat(format);
        cal.setTimeInMillis(System.currentTimeMillis());
        date_str = sd.format(cal.getTime());

        return date_str;
    }    
}
