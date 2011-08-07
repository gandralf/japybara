package japybara;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class WebIntegrationTestTest extends WebIntegrationTest {
    @Test
    public void shouldHit() throws IOException {
        visit("/hello");
        assertTrue(HelloServlet.isHitted());
    }

    @Test
    public void shouldReturnContent() throws IOException {
        WebPage page = visit("/hello");
        assertTrue(page.hasContent("Hello"));
    }

    @Test
    public void shouldHandleHttpStuff() throws IOException {
        WebPage page = visit("/hello?name=John");
        assertEquals("/hello", getCurrentPath()); // Parameters
        assertTrue(page.hasContent("John"));
        // assertEquals("text/html", page.getContentType());
    }
}
