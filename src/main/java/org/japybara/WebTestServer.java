package org.japybara;

import java.net.URL;

/**
 * Contains WebTest required services
 * @param <T> real web server implementation instance, like {@link org.mortbay.jetty.Server}
 */
public interface WebTestServer<T> {
    void start(URL contextUrl, String webappPath) throws Exception;

    boolean isStarted();

    void stop() throws Exception;

    public T getWebServer();
}
