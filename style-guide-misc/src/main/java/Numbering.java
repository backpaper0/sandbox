import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * マークダウンの見出しに1.とか2.3.みたいな番号を振っていくツール。
 * 番号は数字とピリオドで構成する。
 * 既に振られていた場合も無視して番号を振る。
 *
 */
public class Numbering {

    public static void main(final String[] args) throws Exception {

        final Path file = Paths.get(args[0]);

        final Numbering n = new Numbering(Boolean.parseBoolean(args[1]));

        final List<String> lines = Files.readAllLines(file);

        final List<String> newLines = new ArrayList<>();

        final Iterator<String> it = lines.iterator();
        while (it.hasNext()) {
            final String line = it.next();
            if (line.startsWith("```")) {
                newLines.add(line);
                while (it.hasNext()) {
                    final String line2 = it.next();
                    newLines.add(line2);
                    if (line2.startsWith("```")) {
                        break;
                    }
                }
            } else if (line.startsWith("##")) {
                newLines.add(n.convert(line));
            } else {
                newLines.add(line);
            }
        }

        Files.write(file, newLines);
    }

    private static final Pattern p = Pattern.compile("^(#+)\\s+(<a\\s+[^>]+>)?(\\d+\\.)*(.+)$");

    private final List<Integer> numbers = new ArrayList<>();

    private final boolean addName;

    public Numbering(final boolean addName) {
        this.addName = addName;
    }

    String convert(final String heading) {
        final Matcher m = p.matcher(heading);
        if (m.matches() == false) {
            throw new RuntimeException(heading);
        }
        final String mark = m.group(1);
        final String openTag = m.group(2);
        final String already = m.group(3);
        final String text;
        if (openTag != null) {
            final String s = m.group(4);
            text = s.substring(0, s.length() - "</a>".length());
        } else {
            text = m.group(4);
        }
        //        if (already != null) {
        //            numbers.clear();
        //            Arrays.stream(already.split("\\.")).map(a -> Integer.parseInt(a)).forEach(numbers::add);
        //            return heading;
        //        }
        if (numbers.isEmpty()) {
            numbers.add(1);
        } else {
            updateNumbers(mark);
        }
        return format(mark, text);
    }

    private void updateNumbers(final String mark) {
        while (true) {
            final int level = numbers.size() + 1;
            if (level < mark.length()) {
                //first child
                numbers.add(1);
                return;
            }
            if (level == mark.length()) {
                final Integer current = numbers.remove(numbers.size() - 1);
                numbers.add(current + 1);
                return;
            }
            //up to parent
            numbers.remove(numbers.size() - 1);
        }
    }

    private String format(final String mark, final String text) {
        final String no = numbers.stream().map(a -> a.toString())
                .collect(Collectors.joining(".", "", "."));
        final String name = numbers.stream().map(a -> a.toString())
                .collect(Collectors.joining("-", "no", ""));

        if (addName) {
            return String.format("%1$s <a name=\"%2$s\">%3$s%4$s</a>", mark, name, no, text);
        }
        return String.format("%1$s %2$s%3$s", mark, no, text);
    }
}