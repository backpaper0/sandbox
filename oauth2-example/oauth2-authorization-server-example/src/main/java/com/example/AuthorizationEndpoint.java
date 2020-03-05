package com.example;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/authorize")
public class AuthorizationEndpoint extends HttpServlet {

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {

        final Map<String, String> queryParams = Arrays
                .stream(req.getQueryString().split("&"))
                .map(a -> a.split("="))
                .collect(Collectors.toMap(a -> a[0],
                        a -> URLDecoder.decode(a[1], StandardCharsets.UTF_8)));

        final String responseType = queryParams.get("response_type");
        final String clientId = queryParams.get("client_id");
        final String redirectUri = queryParams.get("redirect_uri");
        final String scope = queryParams.get("scope");
        final String state = queryParams.get("state");

        if (Objects.equals(responseType, "code") == false) {
            resp.sendError(400, "Invalid response type: " + responseType);
            return;
        }

        //TODO clientIdからクライアントの情報を取得する。redirect_uriはクライアントの情報に含まれる。

        if (redirectUri == null || redirectUri.startsWith("http://localhost:8080/") == false) {
            resp.sendError(400, "Invalid redirect URI: " + redirectUri);
            return;
        }

        resp.sendRedirect(
                resp.encodeRedirectURL(redirectUri + "?code=examplegrantcode&state=" + state));

        //        final HttpSession session = req.getSession();
        //        session.setAttribute("redirectUri", redirectUri);
        //        session.setAttribute("state", state);

        //        resp.setContentType("text/html; charset=UTF-8");
        //        try (PrintWriter out = resp.getWriter()) {
        //            out.print("<html>");
        //            out.print("<head>");
        //            out.print("<title>OAuth 2.0 - Authorization</title>");
        //            out.print("</head>");
        //            out.print("<body>");
        //            out.print("<form>");
        //            out.print("</form>");
        //            out.print("</body>");
        //            out.print("</html>");
        //        }
    }
}
