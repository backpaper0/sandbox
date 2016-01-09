import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RFC1123Sample {

    public static void main(String[] args) {

        OffsetDateTime dateTime = LocalDateTime.now()
                .atOffset(ZoneOffset.ofHours(9));
        System.out.println(dateTime);

        System.out.println(dateTime.withOffsetSameInstant(ZoneOffset.UTC)
                .format(DateTimeFormatter.RFC_1123_DATE_TIME));

        Date date = Timestamp.valueOf(dateTime.toLocalDateTime());
        System.out.println(date);

        SimpleDateFormat sdf = new SimpleDateFormat(
                "EEE, d MMM yyyy HH:mm:ss zzz", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        System.out.println(sdf.format(date));
    }
}