package parser.combinator;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ParseException extends RuntimeException {

    public ParseException(final ParseContext context, final String message) {
        super(buildMessage(context, message));
    }

    private static String buildMessage(final ParseContext context, final String message) {
        final StringWriter s = new StringWriter();
        final PrintWriter out = new PrintWriter(s);
        out.println(message);
        out.print("> ");
        out.println(context.getInput());
        out.print("> ");
        for (int i = 0; i < context.getPosition() - 1; i++) {
            out.print(' ');
        }
        out.print('^');
        return s.toString();
    }
}
