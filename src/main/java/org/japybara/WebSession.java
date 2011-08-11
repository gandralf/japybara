package org.japybara;

import java.io.IOException;
import java.net.URL;

public abstract class WebSession {
    private URL contextUrl;
    private URL currentUrl;
    private WebPage currentPage;

    public WebSession(URL contextUrl) {
        this.contextUrl = contextUrl;
        currentUrl = null;
    }

    /**
     * Navigate to the given URL.
     * @param path where to go
     * @return the resulting page
     * @throws java.net.MalformedURLException on invalid path
     */
    public abstract WebPage visit(String path) throws IOException;

    /**
     * @return Fully qualified URL of the current page.
     */
    public URL getCurrentURL() {
        return currentUrl;
    }

    protected void setCurrentUrl(URL value) {
        currentUrl = value;
    }

    protected URL getContextUrl() {
        return contextUrl;
    }

    public WebPage getCurrentPage() {
        return currentPage;
    }

    protected void setCurrentPage(WebPage currentPage) {
        this.currentPage = currentPage;
    }
}


    /*
    - (Object) document
    - (Object) driver
    - (Object) evaluate_script(script)
    Evaluate the given JavaScript and return the result.
    - (Object) execute_script(script)
    Execute the given script, not returning a result.
    - (WebSession) initialize(mode, app = nil) constructor
    A new instance of WebSession.
    - (Object) inspect
    - (Object) reset! (also: #cleanup!, #reset_session!)
    Reset the session, removing all cookies.
    - (Hash{String => String}) response_headers
    Returns a hash of response headers.
    - (Object) save_and_open_page
    - (Object) save_page
    Save a snapshot of the page and open it in a browser for inspection.
    - (String) source
    HTML source of the document, before being modified by JavaScript.
    - (Integer) status_code
    Returns the current HTTP status code as an Integer.
    - (Object) wait_until(timeout = Capybara.default_wait_time)
    Retry executing the block until a truthy result is returned or the timeout time is exceeded.
    - (Object) within(*args)
    Execute the given block for a particular scope on the page.
    - (Object) within_fieldset(locator)
    Execute the given block within the a specific fieldset given the id or legend of that fieldset.
    - (Object) within_frame(frame_id)
    Execute the given block within the given iframe given the id of that iframe.
    - (Object) within_table(locator)
    Execute the given block within the a specific table given the id or caption of that table.
    - (Object) within_window(handle, &blk)
    Execute the given block within the given window.
    */
