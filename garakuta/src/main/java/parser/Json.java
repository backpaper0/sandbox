package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Json {

    private static final char[] TRUE = "true".toCharArray();

    private static final char[] FALSE = "false".toCharArray();

    private static final char[] NULL = "null".toCharArray();

    private final char[] cs;

    private char c;

    private int index;

    public Json(final String text) {
        cs = text.toCharArray();
        c = cs[index = 0];
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
            next();
        }
    }

    private void expect(final char c) throws JsonException {
        if (this.c != c) {
            throw new JsonException(this);
        }
        next();
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
                next();
            }
            skipWhitespace();
            if (comma == false && c != '}') {
                throw new JsonException(this);
            }
        }
        next();
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
                next();
            }
            skipWhitespace();
            if (comma == false && c != ']') {
                throw new JsonException(this);
            }
        }
        next();
        return list;
    }

    private String string() throws JsonException {
        final StringBuilder sb = new StringBuilder();
        expect('\"');
        while (c != '"') {
            if ((c == '\\')) {
                next();
                if (c == '\"') {
                    sb.append('\"');
                    next();
                } else if (c == '\\') {
                    sb.append('\\');
                    next();
                } else if (c == '/') {
                    sb.append('/');
                    next();
                } else if (c == 'b') {
                    sb.append('\b');
                    next();
                } else if (c == 'f') {
                    sb.append('\f');
                    next();
                } else if (c == 'n') {
                    sb.append('\n');
                    next();
                } else if (c == 'r') {
                    sb.append('\r');
                    next();
                } else if (c == 't') {
                    sb.append('\t');
                    next();
                } else if (c == 'u') {
                    next();
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
                        next();
                    }
                    sb.append((char) (cs[0] << 0xc | cs[1] << 0x8 | cs[2] << 0x4 | cs[3]));
                } else {
                    throw new JsonException(this);
                }
            } else {
                sb.append(c);
                next();
            }
        }
        next();
        return sb.toString();
    }

    private double number() throws JsonException {
        final StringBuilder sb = new StringBuilder();
        if (c == '-') {
            sb.append(c);
            next();
        }
        while ('0' <= c && c <= '9') {
            sb.append(c - 48);
            next();
        }
        if (c == '.') {
            sb.append(c);
            next();
            if (('0' <= c && c <= '9') == false) {
                throw new JsonException(this);
            }
            while ('0' <= c && c <= '9') {
                sb.append(c - 48);
                next();
            }
        }
        if (c == 'e' || c == 'E') {
            sb.append(c);
            next();
            if (c == '+' || c == '-') {
                sb.append(c);
                next();
            }
            while ('0' <= c && c <= '9') {
                sb.append(c - 48);
                next();
            }
        }
        return Double.valueOf(sb.toString());
    }

    private void skipWhitespace() throws JsonException {
        while (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
            next();
        }
    }

    private void next() throws JsonException {
        index++;
        if (index < cs.length) {
            c = cs[index];
        }
        if (index > cs.length) {
            throw new JsonException(this);
        }
    }

    public Object get() throws JsonException {
        skipWhitespace();
        if (c == '{') {
            return object();
        } else if (c == '[') {
            return array();
        }
        throw new JsonException(this);
    }

    public static Object get(final String text) throws JsonException {
        final Json json = new Json(text);
        return json.get();
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

        public JsonException(final Json json) {
            super(json.toString());
        }
    }
}
