package org.japybara;

import org.junit.Test;

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
    public void shouldManipulateForm() throws IOException {
        WebPage page = visit("/form.html");
        page.fillIn("name", "John");
        page.clickButton("submit");

        assertEquals("/hello", getCurrentPath());
        assertHasContent("Hello, John![post]");
    }
}
