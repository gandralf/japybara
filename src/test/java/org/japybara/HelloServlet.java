package org.japybara;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HelloServlet extends HttpServlet {
    private static boolean hitted = false;

    public static boolean isHitted() {
        return hitted;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        hitted = true;
        String name = request.getParameter("name");
        if (name == null) {
            name = "world!";
        }

        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.print("Hello, " + name + "!");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
