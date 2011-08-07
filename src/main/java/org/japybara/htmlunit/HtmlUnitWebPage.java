package org.japybara.htmlunit;

import com.gargoylesoftware.htmlunit.Page;
import org.japybara.WebPage;

public class HtmlUnitWebPage implements WebPage {
    private Page htmlUnitPage;

    public HtmlUnitWebPage(Page htmlUnitPage) {
        this.htmlUnitPage = htmlUnitPage;
    }

    public String getBody() {
        return htmlUnitPage.getWebResponse().getContentAsString();
    }

    public Object getContentType() {
        return htmlUnitPage.getWebResponse().getContentType();
    }
}
