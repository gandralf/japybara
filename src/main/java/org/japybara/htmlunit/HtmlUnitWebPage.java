package org.japybara.htmlunit;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import org.japybara.WebPage;

import java.io.IOException;
import java.net.URL;

public class HtmlUnitWebPage implements WebPage {
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

    public void clickButton(String locator) throws IOException {
        HtmlSubmitInput button = (HtmlSubmitInput) locate(locator);
        HtmlUnitWebPage newPage = new HtmlUnitWebPage(session, button.click());

        session.goneTo(newPage);
    }

    public URL getUrl() {
        return page.getUrl();
    }

    private HtmlElement locate(String locator) {
        HtmlPage htmlPage = (HtmlPage) page;

        return htmlPage.getElementById(locator);
    }
}
