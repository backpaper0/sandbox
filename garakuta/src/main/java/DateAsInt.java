import java.time.LocalDate;

public class DateAsInt {

	public static void main(String[] args) {
		LocalDate date = LocalDate.now();
		System.out.println(date);
		int i = toInt(date);
		System.out.println(i);
		date = fromInt(i);
		System.out.println(date);
	}

	static int toInt(LocalDate date) {
		return (date.getYear() * 10000) + (date.getMonthValue() * 100) + date.getDayOfMonth();
	}

	static LocalDate fromInt(int i) {
		int src = i;
		int year = src / 10000;
		src = src - (year * 10000);
		int month = src / 100;
		src = src - (month * 100);
		int dayOfMonth = src;
		return LocalDate.of(year, month, dayOfMonth);
	}
}
