package org.japybara;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JndiServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String jndiString = (String) new InitialContext().lookup("hello");
            response.getWriter().write(jndiString);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}
