import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Inspired from https://github.com/thlorenz/doctoc
public class Doctoc {

    public static void main(final String[] args) throws Exception {

        final Path file = Paths.get("target", "README.md");

        final List<String> lines = Files.readAllLines(file);

        final List<Heading> headings = extractHeadings(lines);

        final List<String> newLines = new ArrayList<>();

        final Iterator<String> it = lines.iterator();
        while (it.hasNext()) {
            final String line = it.next();
            newLines.add(line);
            if (line.contains("<!-- START doctoc -->")) {
                break;
            }
        }

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

        while (it.hasNext()) {
            newLines.add(it.next());
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

        private final int level;
        private final String id;
        private final String text;

        private Heading(final int level, final String id, final String text) {
            this.level = level;
            this.id = id;
            this.text = text;
        }

        public String toMarkdown() {
            final StringBuilder buf = new StringBuilder();
            for (int i = 0; i < (level - 2); i++) {
                buf.append("  ");
            }
            buf.append("- [").append(text).append("](#").append(id).append(")");
            return buf.toString();
        }

        public static Heading tryParse(final String line) {
            final Matcher m1 = p1.matcher(line);
            if (m1.matches()) {
                return new Heading(m1.group(1).length(), m1.group(2), m1.group(3));
            }
            final Matcher m2 = p2.matcher(line);
            if (m2.matches()) {
                return new Heading(m2.group(1).length(), m2.group(2), m2.group(2));
            }
            return null;
        }
    }
}
