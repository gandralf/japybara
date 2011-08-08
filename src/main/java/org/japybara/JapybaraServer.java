package org.japybara;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

import java.net.URL;

public class JapybaraServer {
    private static Server server;
    private static String url;
    private static String webappPath;

    public static void main(String[] args) throws Exception {
        if (setup(args)) {
            URL contextUrl = new URL(url);
            server = new Server(contextUrl.getPort());
            WebAppContext webapp = new WebAppContext(webappPath, contextUrl.getPath());

            server.addHandler(webapp);

            server.start();
        } else {
            showUsage();
        }
    }

    private static boolean setup(String[] args) {
        boolean ok = true;
        if (args.length == 0 || args.length == 2 || args.length == 4) {
            url = "http://localhost:8080";
            webappPath = "./src/main/webapp";

            for(int i=0; i < args.length; i++) {
                if("-p".equals(args[i])) {
                    webappPath = args[++i];
                } else if ("-u".equals(args[i])) {
                    url = args[++i];
                } else {
                    ok = false;
                }
            }
        } else {
            ok = false;
        }

        return ok;
    }

    private static void showUsage() {
        System.err.println("Usage: org.japybara.JappybaraServer [-p path] [-u url]\n" +
                "where \n" +
                "\t-p path    Webapp path. Default: ./src/main/webapp\n" +
                "\t-u url     Context url. Default: http://localhost:8080");
    }

    public static void stop() throws Exception {
        server.stop();
    }
}
