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

/**
 * Base class for comprehensive web tests. It can be used in different kinds of tests: functional, integration
 * and even acceptance tests.
 *
 * Those tests not only runs your java code, like a tipical unit tests. It also uses all web related files, like
 * <code>WEB-INF/web.xml</code>, templates/views (jsps) and assets (javascript, css, images, ...).
 * So, it's like startting up and doing a full deploy in your preferred web container, like tomcat or jetty
 * before running each web test. But it is automatic and really fast.
 *
 * The following code ilustrates its use.
 *
 * <pre>{@code
 * public class SomeIntegrationTest extends WebTest {
 *     &#064;Test
 *     public void shouldHit() throws IOException {
 *         visit("/hello/jmalk");
 *         assertHasContent("Welcome back, Mr. Malkovich!");
 *     }
 * }
 * }</pre>
 *
 * So, running this simple test case actually does the following:
 * <ol>
 *     <li>Starts a web container (jetty) on port 8080</li>
 *     <li>Deploys your web application in the context "/". So, it is accessible at URL http://localhost:8080/</li>
 *     <li>Runs <code>shouldHit()</code> tests, wich means:
 *     <ol>
 *         <li><code>visit("/hello/jmalk")</code> makes an http request (GET) to
 *          <code>http://localhost:8080/hello/jmalk</code></li>
 *         <li><code>assertHasContent("Welcome back, Mr. Malkovich!")</code> verifies if the returning HTML contains
 *          the substring <code>"Welcome back, Mr. Malkovich!"</code>.</li>
 *     </ol>
 *     <li>Kills the web container.</li>
 * </ol>
 *
 * Aditionally, your can configure (use the -D jvm parameter):
 * <ul>
 * <li><em>japybara.app_host</em>: basic url (host, port, context). Default: <code>http://localhost:8080/</code></li>
 * <li><em>japybara.webapp</em>: path to webapp directory. Default: <code>src/main/webapp</code></li>
 * </ul>
 */
public class WebTest {
    protected static Server server;
    private static URL contextUrl;
    private Session session;

    @BeforeClass
    public static void startServer() throws Exception {
        contextUrl = new URL(System.getProperty("japybara.app_host", "http://localhost:8080/"));
        String webappPath = System.getProperty("japybara.webapp", "./src/main/webapp");

        server = new Server(contextUrl.getPort());
        WebAppContext webapp = new WebAppContext(webappPath, contextUrl.getPath());

        server.addHandler(webapp);

        server.start();
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
