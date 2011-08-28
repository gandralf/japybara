package org.japybara;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.junit.Before;
import org.openqa.selenium.NoSuchElementException;
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
 * So, it's like starting up and doing a full deploy in your preferred web container, like tomcat or jetty
 * before running each web test. But it is automatic and really fast.
 *
 * The following code illustrates its use.
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
 *     <li>Runs <code>shouldHit()</code> tests, which means:
 *     <ol>
 *         <li><code>visit("/hello/jmalk")</code> makes an http request (GET) to
 *          <code>http://localhost:8080/hello/jmalk</code></li>
 *         <li><code>assertHasContent("Welcome back, Mr. Malkovich!")</code> verifies if the returning HTML contains
 *          the substring <code>"Welcome back, Mr. Malkovich!"</code>.</li>
 *     </ol>
 * </ol>
 */
public class WebTestCase {
    protected static WebTestServer server;
    private static URL contextUrl;
    private WebDriver driver;
    public enum PredefinedHtmlUnitDrivers { Firefox, IE8 }

    /**
     * Starts jetty server if isn't already running.
     * You can override this method to change its behaviour.
     * Additionally, your can configure (use the -D jvm parameter):
     * <ul>
     * <li><em>japybara.app_host</em>: basic url (host, port, context). Default: <code>http://localhost:8080/</code></li>
     * <li><em>japybara.webapp</em>: path to webapp directory. Default: <code>src/main/webapp</code></li>
     * </ul>
     * @throws Exception on Jetty stuff, like port already used, bad configuration, illegal web directory, etc.
     */
    @Before
    public void startServer() throws Exception {
        contextUrl = new URL(System.getProperty("japybara.app_host", "http://localhost:8080/"));
        String webappPath = System.getProperty("japybara.webapp", "./src/main/webapp");

        if (server == null || !server.isStarted()) {
            server = new JettyServer();
            server.start(contextUrl, webappPath);
        }
    }

    /**
     * Creates a HtmlUnitDriver emulating IE8. Override this method to use another one.
     */
    @Before
    public void setUpWebDriver() {
        setDriver(PredefinedHtmlUnitDrivers.IE8);
    }

    public void setDriver(PredefinedHtmlUnitDrivers driverKind) {
        HtmlUnitDriver htmlUnitDriver;
        switch (driverKind) {
            case Firefox: htmlUnitDriver = new HtmlUnitDriver(BrowserVersion.FIREFOX_3_6); break;
            case IE8: htmlUnitDriver = new HtmlUnitDriver(BrowserVersion.INTERNET_EXPLORER_8); break;
            default: throw new IllegalArgumentException("Not implemented: " + driverKind);
        }

        htmlUnitDriver.setJavascriptEnabled(true);

        setDriver(htmlUnitDriver);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }


    // Helper methods

    /**
     * Goes to the specified page (GET)
     * @param path to the page, like '/customers/john'
     * @see WebTestCase#guessElement(String)
     * @throws java.io.IOException on {@link WebDriver#get} exception or if the path is illegal
     */
    public void visit(String path) throws IOException {
        driver.get(new URL(contextUrl, path).toString());
    }

    /**
     * Simulates a click on the object identified by locator
     * @param locator a string that identifies the object to click
     * @see WebTestCase#guessElement(String)
     */
    public void click(String locator) {
        WebElement element = guessElement(locator);
        element.click();
    }

    /**
     * Acts like an user typing <code>content</code> in the field identified by <code>locator</code>.
     * And just before the "typing", there is an click event too.
     * @param locator to identify the input component
     * @param content content to be typed. It will override (clear) the previews content
     * @see WebTestCase#guessElement(String)
     */
    public void fillIn(String locator, String content) {
        WebElement element = guessElement(locator);
        element.click();
        element.clear();
        element.sendKeys(content);
    }

    /**
     * Finds an object using a kind of fussy approach. It works if the given <code>locator</code> is an
     * <code>id</code> or <code>name</code> attribute, or if it identifies a <em>label</em> (so it returns the
     * target field), or CSS/JQuery locator style (like <code>#users li.enabled</code>), the link content (the text
     * within <code>&lt;a&lt;</code> tag) or <code>href</code> target.
     * @param locator identifies (by id, name, ...) the html object.
     * @return the WebElement object found
     * @throws NoSuchElementException if no element has been found for <code>locator</code>
     */
    public WebElement guessElement(String locator) {
        ElementFinder finder = new ElementFinder(driver);
        return finder.guessElement(locator);
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

    /**
     * Checks if the current path is the same as <code>expected</code>
     * @param expected the path. Ignores parameters, so "/user/john" is just like "/user/john?key=3421sd"
     */
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
