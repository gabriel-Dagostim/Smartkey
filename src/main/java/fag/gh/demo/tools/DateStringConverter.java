package fag.gh.demo.tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateStringConverter {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime convert(String data) {
        return LocalDateTime.parse(data, formatter);
    }

    public static String convert(LocalDateTime data) {
        return data.format(formatter);
    }
}
