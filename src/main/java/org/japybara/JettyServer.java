package org.japybara;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Default jetty implementation
 */
public class JettyServer implements WebTestServer<Server> {
    private Server server;
    public static JettyServer mainInstance;

    public static void main(String[] args) throws Exception {
        Config config = new Config(args);
        if (config.ok) {
            mainInstance = new JettyServer();
            mainInstance.start(config.contextUrl, config.webappPath);
        } else {
            config.showUsage();
        }
    }

    public void start(URL contextUrl, String webappPath) throws Exception {
        server = new Server(contextUrl.getPort());
        WebAppContext webapp = new WebAppContext(webappPath, contextUrl.getPath());

        server.addHandler(webapp);

        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public Server getWebServer() {
        return server;
    }


    public boolean isStarted() {
        return server.isStarted();
    }

    private static class Config {
        URL contextUrl;
        String webappPath;
        private boolean ok;

        private Config(String[] args) throws MalformedURLException {
            contextUrl = new URL("http://localhost:8080");
            webappPath = "./src/main/webapp";
            ok = true;
            if (args.length == 0 || args.length == 2 || args.length == 4) {
                for(int i=0; i < args.length; i++) {
                    if("-p".equals(args[i])) {
                        webappPath = args[++i];
                    } else if ("-u".equals(args[i])) {
                        contextUrl = new URL(args[++i]);
                    } else {
                        ok = false;
                    }
                }
            } else {
                ok = false;
            }
        }

        private void showUsage() {
            System.err.println("Usage: org.japybara.JettyServer [-p path] [-u url]\n" +
                    "where \n" +
                    "\t-p path    Webapp path. Default: ./src/main/webapp\n" +
                    "\t-u url     Context url. Default: http://localhost:8080");
        }
    }
}
