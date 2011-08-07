package org.japybara;

import org.japybara.htmlunit.HtmlUnitSession;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class WebIntegrationTest {
    protected static Server server;
    private static String context;
    private static int port;
    private Session session;

    @BeforeClass
    public static void startServer() throws Exception {
        port = 8080;
        server = new Server(port);
        WebAppContext webapp = new WebAppContext("./src/test/webapp", "/");

        server.addHandler(webapp);

        server.start();
    }

    @AfterClass
    public static void stopServer() throws Exception {
        server.stop();
    }

    @Before
    public void setUp() throws MalformedURLException {
        session = new HtmlUnitSession(new URL("http://localhost:" + port));
    }

    public WebPage visit(String path) throws IOException {
        return session.visit(path);
    }

    // Helper methods
    public String getCurrentPath() {
        return session.getCurrentURL().getPath();
    }

    /**
     * @return a snapshot of the HTML of the current document, as it looks right now
     *      (potentially modified by JavaScript).
     */
    public String getBody() {
        return session.getCurrentPage().getBody();
    }

    public boolean hasContent(String str) {
        return getBody().contains(str);
    }

}
