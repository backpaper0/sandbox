package app;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Predicate;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = "/*")
public class CookieLog implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        System.out.println("**** Request ****");
        HttpServletRequest req = (HttpServletRequest) request;
        Arrays.stream(req.getCookies())
                .map(c -> String.format("%s=%s%n", c.getName(), c.getValue()))
                .forEach(System.out::println);

        chain.doFilter(request, response);

        System.out.println("**** Response ****");
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.getHeaderNames().stream().filter(Predicate.isEqual("Set-Cookie")).map(resp::getHeaders)
                .forEach(System.out::println);
    }

    @Override
    public void destroy() {
    }
}
