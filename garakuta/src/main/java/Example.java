import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Example {

	static class LessonClass {

		int dayOfWeek;

		List<Integer> emps = new ArrayList<>();
	}

	static class Request {

		int emp;

		List<Integer> dayOfWeeks;

		static Request parse(final String src) {
			final String[] s = src.split("_");
			final Request r = new Request();
			r.emp = Integer.parseInt(s[0]);
			r.dayOfWeeks = s[1].chars().map(i -> i - '0').boxed()
					.collect(Collectors.toList());
			return r;
		}
	}

	static final int MONDAY = 1;

	static final int FRIDAY = 5;

	public String solve(final String src) {

		final List<Request> requests = Pattern.compile("\\|").splitAsStream(src)
				.map(Request::parse).collect(Collectors.toList());

		final List<LessonClass> lessonClasses = IntStream.rangeClosed(MONDAY, FRIDAY)
				.mapToObj(dayOfWeek -> {
					final LessonClass lc = new LessonClass();
					lc.dayOfWeek = dayOfWeek;
					return lc;
				}).collect(Collectors.toList());

		//Aさんの第一希望、Bさんの第一希望、Cさんの第一希望、Aさんの第二希望
		//というふうに第一希望から順に埋めていく
		final Set<Integer> added = new HashSet<>();
		IntStream.rangeClosed(0, FRIDAY - MONDAY).forEach(i -> {
			requests.forEach(r -> {

				//既にレッスンの曜日は決まっている社員さん
				if (added.contains(r.emp)) {
					return;
				}

				final int index = r.dayOfWeeks.get(i) - MONDAY;
				final LessonClass lc = lessonClasses.get(index);

				//その曜日のレッスンは定員に達している
				if (lc.emps.size() >= 4) {
					return;
				}

				lc.emps.add(r.emp);
				added.add(r.emp);
			});
		});

		return lessonClasses
				.stream()
				.filter(lc -> lc.emps.isEmpty() == false)
				.map(lc -> lc.dayOfWeek
						+ "_"
						+ lc.emps.stream().sorted().map(Object::toString)
								.collect(Collectors.joining(":")))
				.collect(Collectors.joining("|"));
	}
}
