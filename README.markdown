Japybara - integration tests made easy
======================================

Japybara is a simple library intented to speedup integration testes. What it does:

* It starts a jetty containter (fast!) before your integration tests, and kills it after.
You can even interact with the server at (by default) http://localhost:8080/
* Offers a really simple API: just create child of `WebIntegrationTest` and have fun:

    public class SomeIntegrationTest extends WebIntegrationTest {
        @Test
        public void shouldHit() throws IOException {
            visit("/hello?name=John");
            assertTrue(hasContent("Hello, John!"));
        }
    }


It grabs ideas from some rails test frameworks (mostly [Capybara](https://github.com/jnicklas/capybara))
and [JWebUnit](http://jwebunit.sourceforge.net/).

TO DO
=====
I've just started this project, so there are LOTS of things to do, like to provide a (much!) richer DLS,
pluggable web drivers (right now, only have an implementation of HtmlUnit), etc.

Oh, and change its name. Japybara is suposed to be something like a java version of capybara... but is ugly as hell.