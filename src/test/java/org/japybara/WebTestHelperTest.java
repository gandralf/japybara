package org.japybara;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.japybara.WebTestHelper.assertHasContent;
import static org.japybara.WebTestHelper.newSession;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WebTestHelperTest {
    private WebSession session;

    @Before
    public void setUp() throws WebTestException {
        session = newSession();
    }

    @Test
    public void shouldHit() throws IOException {
        session.visit("/hello");
        assertTrue(HelloServlet.isHitted());
    }

    @Test
    public void shouldReturnContent() throws IOException {
        WebPage page = session.visit("/hello");
        assertHasContent(page, "Hello");
    }

    @Test
    public void shouldHandleHttpStuff() throws IOException {
        WebPage page = session.visit("/hello?name=John");
        assertEquals("/hello", session.getCurrentURL().getPath());
        assertHasContent(page, "John");
        assertEquals("text/plain", page.getContentType());
    }

    @Test
    public void shouldRenderJSP() throws IOException {
        WebPage page = session.visit("/view");
        assertHasContent(page, "Hello, John Malkovich!");
    }

    @Test
    public void shouldManipulateForm() throws IOException {
        WebPage page = session.visit("/form.html");
        page.fillIn("name", "John");
        page.clickButton("submit");


        assertEquals("/hello", session.getCurrentURL().getPath());
        assertHasContent(session.getCurrentPage(), "Hello, John![post]");
    }
}
