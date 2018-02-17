package com.example;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.Controller;

public class HelloController implements Controller {

    private final View view = new View() {

        @Override
        public void render(final Map<String, ?> model, final HttpServletRequest request,
                final HttpServletResponse response) throws Exception {
            response.getWriter().println("Hello, world!!!");
        }

        @Override
        public String getContentType() {
            return "text/plain";
        }
    };

    @Override
    public ModelAndView handleRequest(final HttpServletRequest request,
            final HttpServletResponse response)
            throws Exception {
        return new ModelAndView(view);
    }
}
