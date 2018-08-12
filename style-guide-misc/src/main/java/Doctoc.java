import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// Inspired from https://github.com/thlorenz/doctoc
public class Doctoc {

    public static void main(final String[] args) throws Exception {

        final Path file = Paths.get(args[0]);

        final List<String> lines = Files.readAllLines(file);

        final List<Heading> headings = extractHeadings(lines);

        final List<String> newLines = new ArrayList<>();

        final Iterator<String> it = lines.iterator();
        while (it.hasNext()) {
            boolean insertDoc = false;
            while (it.hasNext()) {
                final String line = it.next();
                newLines.add(line);
                if (line.startsWith("```")) {
                    while (it.hasNext()) {
                        final String line2 = it.next();
                        newLines.add(line2);
                        if (line2.startsWith("```")) {
                            break;
                        }
                    }
                } else if (line.contains("<!-- START doctoc -->")) {
                    insertDoc = true;
                    break;
                }
            }

            if (insertDoc) {
                newLines.add("");
                for (final Heading heading : headings) {
                    newLines.add(heading.toMarkdown());
                }
                newLines.add("");

                while (it.hasNext()) {
                    final String line = it.next();
                    if (line.contains("<!-- END doctoc -->")) {
                        newLines.add(line);
                        break;
                    }
                }
            }
        }

        Files.write(file, newLines);
    }

    private static List<Heading> extractHeadings(final List<String> lines) {
        final List<Heading> headings = new ArrayList<>();
        final Iterator<String> it = lines.iterator();
        while (it.hasNext()) {
            final String line = it.next();
            final Heading heading = Heading.tryParse(line);
            if (heading != null) {
                headings.add(heading);
            } else if (line.startsWith("```")) {
                while (it.hasNext()) {
                    if (it.next().startsWith("```")) {
                        break;
                    }
                }
            }
        }
        return headings;
    }

    private static class Heading {

        private static final Pattern p1 = Pattern
                .compile("^(##+)\\s+<a\\s*name\\s*=\\s*\"([^\"]+)\"[^>]*>\\s*([^<]+)\\s*</a>\\s*$");
        private static final Pattern p2 = Pattern
                .compile("^(##+)\\s+(.+)\\s*$");
        private static final int ROOT_LEVEL = 2;

        private final int level;
        private final String id;
        private final String text;

        private Heading(final int level, final String id, final String text) {
            this.level = level;
            this.id = id;
            this.text = text;
        }

        public String toMarkdown() {
            final String indent = IntStream.range(ROOT_LEVEL, level).mapToObj(a -> "  ")
                    .collect(Collectors.joining());
            return String.format("%1$s- [%2$s](#%3$s)", indent, text, id);
        }

        public static Heading tryParse(final String line) {
            final Matcher m1 = p1.matcher(line);
            if (m1.matches()) {
                final int level = m1.group(1).length();
                final String id = m1.group(2);
                final String text = m1.group(3);
                return new Heading(level, id, text);
            }
            final Matcher m2 = p2.matcher(line);
            if (m2.matches()) {
                final int level = m2.group(1).length();
                final String id = m2.group(2);
                final String text = id;
                return new Heading(level, id, text);
            }
            return null;
        }
    }
}
