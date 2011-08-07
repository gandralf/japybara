package org.japybara;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servet to test the (jsp) view, request attributes inspection, etc
 */
public class ViewServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DummyUser user = new DummyUser(33, "jmalk", "John Malkovich");
        req.setAttribute("user", user);

        req.getRequestDispatcher("/pretty.jsp").forward(req, resp);
    }

    public static class DummyUser {
        private int id;
        private String login;
        private String name;

        public DummyUser(int id, String login, String name) {
            this.id = id;
            this.login = login;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getLogin() {
            return login;
        }

        public String getName() {
            return name;
        }
    }
}
