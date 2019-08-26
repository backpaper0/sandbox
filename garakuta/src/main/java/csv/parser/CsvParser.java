package csv.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import csv.lex.CsvLexer;
import csv.lex.CsvToken;

public class CsvParser {

    private final CsvLexer lexer;
    private CsvToken token;

    public CsvParser(final CsvLexer lexer) throws IOException {
        this.lexer = Objects.requireNonNull(lexer);
        consume();
    }

    private void consume() throws IOException {
        token = lexer.next();
    }

    public Optional<List<String>> next() throws IOException {
        if (token.isEof()) {
            return Optional.empty();
        }
        final List<String> fields = new ArrayList<>();
        if (token.isLineBreak()) {
            consume();
            return Optional.of(fields);
        } else if (token.isSeparator()) {
            while (token.isSeparator()) {
                fields.add("");
                consume();
            }
            if (token.isLineBreak()) {
                fields.add("");
                consume();
                return Optional.of(fields);
            } else if (token.isEof()) {
                fields.add("");
                return Optional.of(fields);
            }
        }
        while (true) {
            fields.add(token.text);
            consume();
            if (token.isEof()) {
                return Optional.of(fields);
            } else if (token.isLineBreak()) {
                consume();
                return Optional.of(fields);
            } else if (token.isSeparator()) {
                consume();
                while (token.isSeparator()) {
                    fields.add("");
                    consume();
                }
                if (token.isLineBreak()) {
                    fields.add("");
                    consume();
                    return Optional.of(fields);
                } else if (token.isEof()) {
                    fields.add("");
                    return Optional.of(fields);
                }
            }
        }
    }
}
