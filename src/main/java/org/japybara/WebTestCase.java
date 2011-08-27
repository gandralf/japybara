package org.japybara;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Base class for comprehensive web tests. It can be used in different kinds of tests: functional, integration
 * and even acceptance tests.
 *
 * Those tests not only runs your java code, like a typical unit tests. It also uses all web related files, like
 * <code>WEB-INF/web.xml</code>, templates/views (jsps) and assets (javascript, css, images, ...).
 * So, it's like startting up and doing a full deploy in your preferred web container, like tomcat or jetty
 * before running each web test. But it is automatic and really fast.
 *
 * The following code ilustrates its use.
 *
 * <pre>{@code
 * public class SomeIntegrationTest extends WebTestCase {
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
public class WebTestCase {
    protected static Server server;
    private static URL contextUrl;
    private WebDriver driver;

    @BeforeClass
    public static void startServer() throws Exception {
        contextUrl = new URL(System.getProperty("japybara.app_host", "http://localhost:8080/"));
        String webappPath = System.getProperty("japybara.webapp", "./src/main/webapp");

        if (server == null || !server.isStarted()) {
            server = new Server(contextUrl.getPort());
            WebAppContext webapp = new WebAppContext(webappPath, contextUrl.getPath());

            server.addHandler(webapp);

            server.start();
        }
    }

    @Before
    public void setUp() throws MalformedURLException {
        driver = new HtmlUnitDriver(true);
    }

    public WebDriver getDriver() {
        return driver;
    }

    // Helper methods

    public void visit(String path) throws IOException {
        driver.get(new URL(contextUrl, path).toString());
    }

    public void click(String locator) {
        WebElement element = guessElement(locator);
        element.click();
    }

    public void fillIn(String name, String content) {
        WebElement element = guessElement(name);
        element.click();
        element.clear();
        element.sendKeys(content);
    }

    private WebElement guessElement(String name) {
        ElementFinder finder = new ElementFinder(driver);
        return finder.guessElement(name);
    }

    /**
     * @return a snapshot of the HTML of the current document, as it looks right now
     *      (potentially modified by JavaScript).
     */
    public String getContent() {
        return driver.getPageSource();
    }

    // Assert methods (for cleaner messages)
    protected void assertHasContent(String expected) {
        if (!getContent().contains(expected)) {
            fail("Expected content not found: \"" + expected + "\"");
        }
    }

    protected void assertCurrentPath(String expected) {
        assertEquals(expected, getCurrentPath());
    }

    public String getCurrentPath() {
        try {
            return new URL(driver.getCurrentUrl()).getPath();
        } catch (MalformedURLException e){
            throw new IllegalStateException(e);
        }
    }

    protected void back() {
        driver.navigate().back();
    }
}
