package org.japybara;

import com.gargoylesoftware.htmlunit.WebClient;
import org.junit.Test;
import org.mortbay.jetty.Server;

import java.net.URL;

import static org.junit.Assert.assertTrue;

public class JettyServerTest {
    @Test
    public void shouldUseJetty() throws Exception {
        WebTestServer server = new JettyServer();
        server.start(new URL("http://localhost:8081"), "./src/test/webapp");
        assertTrue(server.getWebServer() instanceof Server);
        server.stop();
    }

    @Test
    public void shouldAcceptAllParams() throws Exception {
        URL url = new URL("http://localhost:8081");
        String[] params = {"-u", url.toString(), "-p", "./src/test/webapp"};
        makeMyDay(url, params);
    }

    @Test
    public void shouldAcceptDefaults() throws Exception {
        URL url = new URL("http://localhost:8080");
        makeMyDay(url, new String[0]);
    }

    @Test
    public void shouldAssumeDefaultUrl() throws Exception {
        URL url = new URL("http://localhost:8080");
        String[] params = {"-p", "./src/test/webapp"};
        makeMyDay(url, params);
    }

    @Test
    public void shouldAssumeDefaultPath() throws Exception {
        URL url = new URL("http://localhost:8081");
        String[] params = {"-u", url.toString()};
        makeMyDay(url, params);
    }

    private void makeMyDay(URL url, String[] params) throws Exception {
        JettyServer.main(params);
        WebClient webClient = new WebClient();
        assertTrue(webClient.getPage(url).getWebResponse().getContentAsString().contains("This is a static html file"));
        JettyServer.mainInstance.stop();
    }
}
