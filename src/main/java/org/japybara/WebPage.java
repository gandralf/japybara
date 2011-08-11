package org.japybara;

import java.io.IOException;
import java.net.URL;

public interface WebPage {
    String getBody();

    Object getContentType();

    void fillIn(String locator, String value);
    void clickButton(String locator) throws IOException;

    URL getUrl();
}
