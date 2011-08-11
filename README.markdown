Japybara - web functional/integration/acceptance tests made easy
================================================================

Japybara is a simple library intented to speedup functional, integration or acceptance testes. What it does:

* It starts a jetty containter (fast!) before your functional tests, and kills it after.
You can even interact with the server at (by default) http://localhost:8080/
* Offers a really simple API: just create child of `WebTest` and have fun.

To test if the request "/hello/jmalk" returns a web page with `"Welcome back, Mr. Malkovich!"`, just do it:

    public class SomeFunctionalTest extends WebTest {
        @Test
        public void shouldHit() throws IOException {
            visit("/hello/jmalk");
            assertHasContent("Welcome back, Mr. Malkovich!");
        }
    }


It grabs ideas from some rails test frameworks (mostly [Capybara](https://github.com/jnicklas/capybara))
and [JWebUnit](http://jwebunit.sourceforge.net/).

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
I've just started this project, so there are LOTS of things to do, like to provide a (much!) richer DLS, configuration
(context, port...), pluggable web drivers (right now, only have an implementation of HtmlUnit), etc.

Oh, and change its name. Japybara is suposed to be something like a java version of capybara... but is ugly as hell.