package app;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet(loadOnStartup = 0)
public class ServletStartup2 extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        Logs.add("HttpServlet");
    }
}
