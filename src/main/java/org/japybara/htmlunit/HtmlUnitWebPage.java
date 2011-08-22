package org.japybara.htmlunit;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.*;
import org.japybara.WebPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class HtmlUnitWebPage implements WebPage {
    private static final Logger log = LoggerFactory.getLogger(HtmlUnitWebPage.class);
    private HtmlUnitSession session;
    private Page page;

    public HtmlUnitWebPage(HtmlUnitSession session, Page page) {
        this.session = session;

        this.page = page;
    }

    public String getBody() {
        return page.getWebResponse().getContentAsString();
    }

    public Object getContentType() {
        return page.getWebResponse().getContentType();
    }

    public void fillIn(String locator, String value) {
        HtmlTextInput element = (HtmlTextInput) locate(locator);
        element.setText(value);
    }

    public void click(String locator) throws IOException {
        HtmlElement element = locate(locator);
        if (element != null) {
            log.debug("Click at " + locator);
            Page pageFromClick = element.click();
            if (!page.equals(pageFromClick)) {
                HtmlUnitWebPage newPage = new HtmlUnitWebPage(session, pageFromClick);
                session.goneTo(newPage);
            }
        } else {
            log.warn("Html element not found: \"" + locator + "\"");
        }
    }

    public URL getUrl() {
        return page.getUrl();
    }

    private HtmlElement locate(String locator) {
        HtmlPage htmlPage = (HtmlPage) page;

        HtmlElement element = htmlPage.getElementById(locator);

        if (element == null) { // Try to find by name
            List<HtmlElement> list = htmlPage.getElementsByName(locator);
            if (list.size() == 1) {
                element = list.get(0);
            } else if (list.size() > 1) {
                log.warn("Can't you be more specific? Found more then one element (" +
                        list.size() + ") with name = " + locator);
            }
        }

        if (element == null) { // Try to find by content
            try {
                element = htmlPage.getAnchorByText(locator);
            } catch (ElementNotFoundException e) {
                // Kind of stupid
            }
        }

        if (element == null) { // Try to find by a href param
            try {
                element = htmlPage.getAnchorByHref(locator);
            } catch (ElementNotFoundException e) {
                // Kinda stupid
            }
        }

        return element;
    }
}
