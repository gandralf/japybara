package org.japybara.htmlunit;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import org.japybara.Session;
import org.japybara.WebPage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class HtmlUnitSession extends Session {
    private WebClient webClient;

    public HtmlUnitSession(URL contextUrl) {
        super(contextUrl);
        webClient = new WebClient();
    }

    @Override
    public WebPage visit(String path) throws IOException {
        Page page = webClient.getPage(buildUrl(path));

        setCurrentUrl(page.getUrl());
        setCurrentPage(new HtmlUnitWebPage(page));

        return getCurrentPage();
    }

    private URL buildUrl(String uri) throws MalformedURLException {
        if (!uri.startsWith("/")) {
            uri = new StringBuilder().append("/").append(uri).toString();
        }
        return new URL(getContextUrl(), uri);
    }
}
