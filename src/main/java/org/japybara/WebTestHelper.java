package org.japybara;

import org.japybara.htmlunit.HtmlUnitWebSession;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

import java.net.URL;

import static org.junit.Assert.fail;

/**
 * Base class for comprehensive web tests. It can be used in different kinds of tests: functional, integration
 * and even acceptance tests.
 *
 * Those tests not only runs your java code, like a typical unit test. It also uses all web related files, like
 * <code>WEB-INF/web.xml</code>, templates/views (jsps) and assets (javascript, css, images, ...).
 * So, it's like startting up and doing a full deploy in your preferred web container, like tomcat or jetty
 * before running each web test. But it is automatic and really fast.
 *
 * The following code ilustrates its usege.
 *
 * <pre>{@code
 * public class SomeFunctionalTest {
 *     private WebSession session;
 *
 *     &#064;Before
 *     public void setUp() throws WebTestException {
 *         session = WebTestHelper.newSession();
 *     }
 *
 *     &#064;Test
 *     public void shouldSayHallo() throws IOException {
 *         WebPage page = session.visit("/hello/jmalk");
 *         WebTestHelper.assertHasContent(page, "Welcome back, Mr. Malkovich!");
 *     }
 * }
 * }</pre>
 *
 * So, running this simple test case actually does the following:
 * <ol>
 *     <li><code>setUp</code> / <code>session = WebTestHelper.newSession();</code>, starts a web container (jetty) on
 *      port 8080, if it is not already running, and returns a {@link WebSession} instance.</li>
 *     <li>Deploys your web application into the "/" context. So, it is accessible at URL http://localhost:8080/</li>
 *     <li>Runs <code>shouldSayHallo()</code> tests, wich means:
 *     <ol>
 *         <li><code>visit("/hello/jmalk")</code> makes an http request (GET) to
 *          <code>http://localhost:8080/hello/jmalk</code></li>
 *         <li><code>assertHasContent("Welcome back, Mr. Malkovich!")</code> verifies if the returning HTML contains
 *          the substring <code>"Welcome back, Mr. Malkovich!"</code>.</li>
 *     </ol>
 * </ol>
 *
 * Aditionally, your can configure (use the -D jvm parameter):
 * <ul>
 * <li><em>japybara.app_host</em>: basic url (host, port, context). Default: <code>http://localhost:8080/</code></li>
 * <li><em>japybara.webapp</em>: path to webapp directory. Default: <code>src/main/webapp</code></li>
 * </ul>
 */
public class WebTestHelper {
    private static Server server;
    private static URL contextUrl;

    public static void startServer() throws Exception {
        contextUrl = new URL(System.getProperty("japybara.app_host", "http://localhost:8080/"));
        String webappPath = System.getProperty("japybara.webapp", "./src/main/webapp");

        server = new Server(contextUrl.getPort());
        WebAppContext webapp = new WebAppContext(webappPath, contextUrl.getPath());

        server.addHandler(webapp);

        server.start();
    }

    public static void stopServer() throws Exception {
        server.stop();
    }

    public static WebSession newSession() throws WebTestException {
        if (server == null || !server.isStarted()) {
            try {
                startServer();
            } catch (Exception e) {
                throw new WebTestException("Can't start server: " + e.toString(), e);
            }
        }

        return new HtmlUnitWebSession(contextUrl);
    }

    public static void assertHasContent(WebPage page, String expected) {
        if (!page.getBody().contains(expected)) {
            fail("Expected content not found: \"" + expected + "\"");
        }
    }
}
