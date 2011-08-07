package japybara;

import com.gargoylesoftware.htmlunit.Page;

public class WebPage {
    private Page htmlUnitPage;

    public WebPage(Page htmlUnitPage) {
        this.htmlUnitPage = htmlUnitPage;
    }

    public boolean hasContent(String str) {
        return htmlUnitPage.getWebResponse().getContentAsString().contains(str);
    }
}
