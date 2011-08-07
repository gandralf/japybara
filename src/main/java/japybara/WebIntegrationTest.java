package japybara;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

import java.io.IOException;
import java.net.URL;

public class WebIntegrationTest {
    protected static Server server;
    private static int port;
    private WebClient webClient;
    private String currentPath;

    @BeforeClass
    public static void startServer() throws Exception {
        port = 8080;
        server = new Server(port);
        WebAppContext webapp = new WebAppContext("./src/test/webapp", "/");

        server.addHandler(webapp);

        server.start();
    }

    @AfterClass
    public static void stopServer() throws Exception {
        server.stop();
    }

    @Before
    public void setUp() {
        webClient = new WebClient();
    }

    public WebPage visit(String path) throws IOException {
        Page page = webClient.getPage(buildUrl(path));
        currentPath = page.getUrl().getPath();

        return new WebPage(page);
    }

    private String buildUrl(String uri) {
        if (!uri.startsWith("/")) {
            uri = new StringBuilder().append("/").append(uri).toString();
        }
        return "http://localhost:" + port + uri;
    }

    // Helper methods
    public String getCurrentPath() {
        return currentPath;
    }
}
