package org.japybara;

import org.junit.Ignore;
import org.junit.Test;

import javax.naming.NamingException;
import java.io.IOException;

import static org.junit.Assert.*;

public class WebTestTest extends WebTest {
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
        WebPage page = visit("/hello?name=John");
        assertEquals("/hello", getCurrentPath()); // Parameters
        assertHasContent("John");
        assertEquals("text/plain", page.getContentType());
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
        WebPage page = visit("/form.html");
        page.fillIn("name", "John");
        page.click("submit");

        assertEquals("/hello", getCurrentPath());
        assertHasContent("Hello, John![post]");
    }

    @Test
    public void shouldFollowLinks() throws IOException {
        WebPage page = visit("/links.html");
        page.click("form-link");

        assertCurrentPath("/form.html");
    }

    @Test
    public void shouldAcceptFlexibleSelectors() throws IOException {
        thereAndBackAgain("id", "form-link");
        thereAndBackAgain("content", "This is the form page");
        thereAndBackAgain("href", "form.html");
        thereAndBackAgain("js content", "Click me");
    }

    private void thereAndBackAgain(String method, String locator) throws IOException {
        WebPage page = visit("/links.html");
        page.click(locator);
        if ("/form.html".equals(getCurrentPath())) {
            assertCurrentPath("/form.html");
            back();
            assertCurrentPath("/links.html");
        } else {
            fail("Locator not found: \"" + locator + "\", method: " + method);
        }
    }
}
