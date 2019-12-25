package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonParser {

    private static final char EOF = (char) -1;

    private static final char[] TRUE = "true".toCharArray();

    private static final char[] FALSE = "false".toCharArray();

    private static final char[] NULL = "null".toCharArray();

    private final char[] cs;

    private char c;

    private int index;

    public JsonParser(final String text) {
        cs = text.toCharArray();
        consume();
    }

    private Object value() throws JsonException {
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
        throw new JsonException(this);
    }

    private void expect(final char[] cs) throws JsonException {
        for (int i = 0; i < cs.length; i++) {
            if (c != cs[i]) {
                throw new JsonException(this);
            }
            consume();
        }
    }

    private void expect(final char c) throws JsonException {
        if (this.c != c) {
            throw new JsonException(this);
        }
        consume();
    }

    private Map<String, Object> object() throws JsonException {
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
                throw new JsonException(this);
            }
        }
        consume();
        return map;
    }

    private List<Object> array() throws JsonException {
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
                throw new JsonException(this);
            }
        }
        consume();
        return list;
    }

    private String string() throws JsonException {
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
                            throw new JsonException(this);
                        }
                        consume();
                    }
                    sb.append((char) (cs[0] << 0xc | cs[1] << 0x8 | cs[2] << 0x4 | cs[3]));
                } else {
                    throw new JsonException(this);
                }
            } else {
                sb.append(c);
                consume();
            }
        }
        consume();
        return sb.toString();
    }

    private double number() throws JsonException {
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
                throw new JsonException(this);
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

    private void skipWhitespace() throws JsonException {
        while (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
            consume();
        }
    }

    private void consume() {
        if (index < cs.length) {
            c = cs[index++];
        } else {
            c = EOF;
        }
    }

    public Object parse() throws JsonException {
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
        throw new JsonException(this);
    }

    public static Object parse(final String text) throws JsonException {
        final JsonParser json = new JsonParser(text);
        return json.parse();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Json(cs = ");
        sb.append(cs);
        sb.append(" index = ");
        sb.append(index);
        sb.append(")");
        sb.append("\n");
        sb.append(cs);
        sb.append("\n");
        for (int i = 0; i < index; i++) {
            sb.append(' ');
        }
        sb.append("^");
        return sb.toString();
    }

    public static class JsonException extends Exception {

        public JsonException(final JsonParser json) {
            super(json.toString());
        }
    }
}
