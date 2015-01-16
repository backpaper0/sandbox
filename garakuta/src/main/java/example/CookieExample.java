package example;

import java.net.HttpCookie;
import java.util.List;

public class CookieExample {

    public static void main(String[] args) {
        List<HttpCookie> cookies = HttpCookie
                .parse("Set-Cookie: foo=bar; Expires=Sun, 06 Nov 1994 08:49:37 GMT");
        System.out.println(cookies);
    }
}
