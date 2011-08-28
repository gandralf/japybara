Japybara - web functional/integration/acceptance tests made easy
================================================================

Japybara is a library intended to make functional tests *as simple as possible*. So, don't expect a
super-flexible API.

What it does:

* It starts a jetty container (fast!) on demmand. You can even interact with the server at
(by default) http://localhost:8080/
* Offers a really simple API: just extends `WebTest` and use methods like `visit`, `fillIn`, `click`, and
`assertHasContent`.

For example, to test if a request to "/hello/jmalk" returns a web page with `"Welcome back, Mr. Malkovich!"`,
just do it:

    public class SomeFunctionalTest extends WebTestCase {
        @Test
        public void shouldSayHello() throws IOException {
            visit("/hello/jmalk");
            assertHasContent("Welcome back, Mr. Malkovich!");
        }
    }

So, running this simple test case actually does the following:
* Starts Jetty (port 8080)
* Deploys your web application in the context "/". So, it is accessible at URL http://localhost:8080/
* `visit("/hello/jmalk")`: makes an http request (GET) to http://localhost:8080/hello/jmalk
* `assertHasContent("Welcome back, Mr. Malkovich!")`: verifies if the returning HTML contains the substring
  "Welcome back, Mr. Malkovich!"

It grabs ideas from some other test frameworks (mostly [Capybara](https://github.com/jnicklas/capybara))
and [JWebUnit](http://jwebunit.sourceforge.net/). It is built on top of [Jetty](http://jetty.codehaus.org/jetty/) and
[WebDriver](http://seleniumhq.org/docs/03_webdriver.html).

Interaction API
===============

Basic interaction
-----------------

The main interaction methods are:

* `visit(String path)`: goes to Goes to the specified page
* `click(String locator)`: simulates a click on the object identified by locator
* `fillIn(String locator, String content)`: acts like an user, typing `content` into the object identified by `locator`
* `back()`: returns to the previews page, like a browsers's 'back' button

The `locator` parameter used in `click` and `fillIn` methods is passed to `guessElement`
to find that element. So, this is the description on `guessElement` method:

`WebElement guessElement(String locator)`
* Finds an object using a kind of fussy approach. It works if the given <code>locator</code> is an `id` or `name`
attribute, or if it identifies a `label` (so it returns the target field), or CSS/JQuery locator style (like
`#users li.enabled`), the link content (the text within `&lt;a&lt;` tag) or `href` target.
* Returns an instance of `WebElement` (from the WebDriver API) or throws `NoSuchElementException` if not found.

Advanced interaction through WebDriver
--------------------------------------

It is possible to program more advanced interactions using the [WebDriver](http://seleniumhq.org/docs/03_webdriver.html)
(aka Selenium) API. The `WebDriver` instance is available throw the `getDriver()` method.

By default, a `HtmlUnitWebDriver` emulating Internet Explorer 8 with javascript enabled is used. To change the default
driver, simply override the `setUpWebDriver()` method (and call `setDriver()` within).

Asserting that everything is OK
-------------------------------

* `assertHasContent(String)`: checks if the page has the given content
* `String getContent()`: return a snapshot of the HTML, as it looks right now (potentially modified by Javascript).
* `assertCurrentPath(String)`: Checks if the current path is the given parameter

Server: Jetty
=============

Before your first test case, there will be a Jetty container ready. You can configure some defaults using `-D` jvm
parameters:
* `japybara.app_host`: context url (host, port, context). Default: `http://localhost:8080/`
* `japybara.webapp`: path to webapp directory. Default: `src/main/webapp` *Tip:* use maven plus
[war:inplace](http://maven.apache.org/plugins/maven-war-plugin/index.html).

In `WebTestCase`, call `server.getWebServer()` to access the Jetty server (`org.mortbay.jetty.Server`). Or rewrite
the `startServer` method to use another implementation of `WebTestServer` other than `JettyServer`.

*Note:* Actually it doesn't support JNDI lookups.

Extra: standalone server
------------------------

There is also a standalone server:

    Usage: org.japybara.JappybaraServer [-p path] [-u url]
    where
        -p path    Webapp path. Default: ./src/main/webapp
        -u url     Context url. Default: http://localhost:8080/

Smells like rails2's script/server

TO DO
=====
I've just started this project, so there are LOTS of things to do.

Oh, and change its name. Japybara is supposed to be something like a java version of capybara... but is ugly as hell.