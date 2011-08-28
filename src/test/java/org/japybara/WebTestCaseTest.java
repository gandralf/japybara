package org.japybara;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import javax.naming.NamingException;
import java.io.IOException;

import static org.junit.Assert.*;

public class WebTestCaseTest extends WebTestCase {
    @AfterClass
    public static void shutDownJetty() throws Exception {
        server.stop();
    }

    @Test
    public void shouldHit() throws IOException {
        visit("/hello");
        assertTrue(HelloServlet.isHitted());
    }

    @Test
    public void shouldReturnContent() throws IOException {
        visit("/hello");
        assertHasContent("Hello");
    }

    @Test
    public void shouldHandleHttpStuff() throws IOException {
        visit("/hello?name=John");
        assertCurrentPath("/hello"); // Parameters
        assertHasContent("John");
        // assertEquals("text/plain", page.getContentType());
    }

    @Test
    public void shouldRenderJSP() throws IOException {
        visit("/view");
        assertHasContent("Hello, John Malkovich!");
    }

    @Test
    @Ignore
    public void shouldLookup() throws NamingException, IOException {
        visit("/jndi");
        assertHasContent("Hello, jndi!");
    }

    @Test
    public void shouldManipulateForm() throws IOException {
        visit("/form.html");
        fillIn("name", "John");
        click("submit");

        assertCurrentPath("/hello");
        assertHasContent("Hello, John![post]");
    }

    @Test
    public void shouldFollowLinks() throws IOException {
        visit("/links.html");
        click("form-link");

        assertCurrentPath("/form.html");
    }

    @Test
    public void shouldAcceptFlexibleSelectors() throws IOException {
        thereAndBackAgain("id", "form-link");
        thereAndBackAgain("content", "This is the form page");
        thereAndBackAgain("href", "form.html");
        thereAndBackAgain("js content", "Click me");
        thereAndBackAgain("selector", "div#content a");
    }

    @Test
    public void shouldAcceptLabels() throws IOException {
        visit("/links.html");
        WebElement login = getDriver().findElement(By.id("login"));
        fillIn("Login", "azdrubal");
        assertEquals("azdrubal", login.getAttribute("value"));
    }

    @Test
    public void shouldUseFirefoxDriver() throws IOException {
        setDriver(PredefinedHtmlUnitDrivers.Firefox);
        shouldAcceptFlexibleSelectors();
    }

    private void thereAndBackAgain(String method, String locator) throws IOException {
        visit("/links.html");
        click(locator);
        if ("/form.html".equals(getCurrentPath())) {
            assertCurrentPath("/form.html");
            back();
            assertCurrentPath("/links.html");
        } else {
            fail("Locator not found: \"" + locator + "\", method: " + method);
        }
    }
}
