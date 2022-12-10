package app;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet(loadOnStartup = 0)
public class ServletStartup2 extends HttpServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {
		Logs.add("HttpServlet");
	}
}
