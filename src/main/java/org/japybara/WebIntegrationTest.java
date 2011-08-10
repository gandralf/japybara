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

import static org.junit.Assert.fail;

public class WebIntegrationTest {
    protected static Server server;
    private static URL contextUrl;
    private Session session;

    @BeforeClass
    public static void startServer() throws Exception {
        contextUrl = new URL(env("japybara.app_host", "http://localhost:8080/"));
        String webappPath = env("japybara.webapp", "./src/main/webapp");

        server = new Server(contextUrl.getPort());
        WebAppContext webapp = new WebAppContext(webappPath, contextUrl.getPath());

        server.addHandler(webapp);

        server.start();
    }

    private static String env(String name, String defaultValue) {
        String result = System.getProperty(name);

        return result != null ? result : defaultValue;
    }

    @AfterClass
    public static void stopServer() throws Exception {
        server.stop();
    }

    @Before
    public void setUp() throws MalformedURLException {
        session = new HtmlUnitSession(contextUrl);
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

    protected void assertHasContent(String expected) {
        if (!getBody().contains(expected)) {
            fail("Expected content not found: \"" + expected + "\"");
        }
    }
}
