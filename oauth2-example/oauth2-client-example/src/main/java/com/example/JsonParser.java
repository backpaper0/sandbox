package com.example;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonParser {

    private static final char EOF = (char) -1;

    private static final char[] TRUE = "true".toCharArray();

    private static final char[] FALSE = "false".toCharArray();

    private static final char[] NULL = "null".toCharArray();

    private final Reader source;

    private char c;

    private int index;

    public JsonParser(final String text) {
        this(new StringReader(text));
    }

    public JsonParser(final Reader source) {
        this.source = source;
        consume();
    }

    private Object value() {
        if (c == '"') {
            return string();
        } else if (('0' <= c && c <= '9') || (c == '-')) {
            return number();
        } else if (c == '{') {
            return object();
        } else if (c == '[') {
            return array();
        } else if (c == 't') {
            expect(TRUE);
            return true;
        } else if (c == 'f') {
            expect(FALSE);
            return false;
        } else if (c == 'n') {
            expect(NULL);
            return null;
        }
        throw new RuntimeException();
    }

    private void expect(final char[] cs) {
        for (int i = 0; i < cs.length; i++) {
            if (c != cs[i]) {
                throw new RuntimeException();
            }
            consume();
        }
    }

    private void expect(final char c) {
        if (this.c != c) {
            throw new RuntimeException();
        }
        consume();
    }

    private Map<String, Object> object() {
        final Map<String, Object> map = new HashMap<>();
        expect('{');
        skipWhitespace();
        while (c != '}') {
            final String key = string();
            skipWhitespace();
            expect(':');
            skipWhitespace();
            final Object value = value();
            skipWhitespace();
            map.put(key, value);
            final boolean comma = (c == ',');
            if (comma) {
                consume();
            }
            skipWhitespace();
            if (comma == false && c != '}') {
                throw new RuntimeException();
            }
        }
        consume();
        return map;
    }

    private List<Object> array() {
        final List<Object> list = new ArrayList<>();
        expect('[');
        skipWhitespace();
        while (c != ']') {
            final Object value = value();
            skipWhitespace();
            list.add(value);
            final boolean comma = (c == ',');
            if (comma) {
                consume();
            }
            skipWhitespace();
            if (comma == false && c != ']') {
                throw new RuntimeException();
            }
        }
        consume();
        return list;
    }

    private String string() {
        final StringBuilder sb = new StringBuilder();
        expect('\"');
        while (c != '"') {
            if ((c == '\\')) {
                consume();
                if (c == '\"') {
                    sb.append('\"');
                    consume();
                } else if (c == '\\') {
                    sb.append('\\');
                    consume();
                } else if (c == '/') {
                    sb.append('/');
                    consume();
                } else if (c == 'b') {
                    sb.append('\b');
                    consume();
                } else if (c == 'f') {
                    sb.append('\f');
                    consume();
                } else if (c == 'n') {
                    sb.append('\n');
                    consume();
                } else if (c == 'r') {
                    sb.append('\r');
                    consume();
                } else if (c == 't') {
                    sb.append('\t');
                    consume();
                } else if (c == 'u') {
                    consume();
                    final int cs[] = new int[4];
                    for (int i = 0; i < cs.length; i++) {
                        if ('0' <= c && c <= '9') {
                            cs[i] = (c - 48);
                        } else if ('a' <= c && c <= 'f') {
                            cs[i] = (c - 87);
                        } else if ('A' <= c && c <= 'F') {
                            cs[i] = (c - 55);
                        } else {
                            throw new RuntimeException();
                        }
                        consume();
                    }
                    sb.append((char) (cs[0] << 0xc | cs[1] << 0x8 | cs[2] << 0x4 | cs[3]));
                } else {
                    throw new RuntimeException();
                }
            } else {
                sb.append(c);
                consume();
            }
        }
        consume();
        return sb.toString();
    }

    private double number() {
        final StringBuilder sb = new StringBuilder();
        if (c == '-') {
            sb.append(c);
            consume();
        }
        while ('0' <= c && c <= '9') {
            sb.append(c - 48);
            consume();
        }
        if (c == '.') {
            sb.append(c);
            consume();
            if (('0' <= c && c <= '9') == false) {
                throw new RuntimeException();
            }
            while ('0' <= c && c <= '9') {
                sb.append(c - 48);
                consume();
            }
        }
        if (c == 'e' || c == 'E') {
            sb.append(c);
            consume();
            if (c == '+' || c == '-') {
                sb.append(c);
                consume();
            }
            while ('0' <= c && c <= '9') {
                sb.append(c - 48);
                consume();
            }
        }
        return Double.valueOf(sb.toString());
    }

    private void skipWhitespace() {
        while (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
            consume();
        }
    }

    private void consume() {
        try {
            c = (char) source.read();
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Object parse() {
        skipWhitespace();
        if (c == '{') {
            final Map<String, Object> o = object();
            expect(EOF);
            return o;
        } else if (c == '[') {
            final List<Object> a = array();
            expect(EOF);
            return a;
        }
        throw new RuntimeException();
    }

    public static Object parse(final String text) {
        final JsonParser json = new JsonParser(text);
        return json.parse();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Json(cs = ");
        sb.append(source);
        sb.append(" index = ");
        sb.append(index);
        sb.append(")");
        sb.append("\n");
        sb.append(source);
        sb.append("\n");
        for (int i = 0; i < index; i++) {
            sb.append(' ');
        }
        sb.append("^");
        return sb.toString();
    }
}