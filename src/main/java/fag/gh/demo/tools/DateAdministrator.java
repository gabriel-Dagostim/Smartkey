package fag.gh.demo.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAdministrator {

    public static Date currentDate() throws ParseException {

        SimpleDateFormat formater = new SimpleDateFormat("yyy-MM-dd");
        String formatedDate = formater.format(new Date());
        Date convertedDate = formater.parse(formatedDate);

        return convertedDate;
    }

}
