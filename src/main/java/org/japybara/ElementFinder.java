package org.japybara;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ElementFinder {
    private WebDriver driver;
    private WebElement foundElement;

    public ElementFinder(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement guessElement(String locator) {
        if (safeFindElement(By.id(locator)) ||
                safeFindElement(By.name(locator)) ||
                safeFindElement(By.cssSelector(locator)) ||
                safeFindElement(By.linkText(locator)) ||
                safeFindElementByHRef(locator)) {
            return foundElement;
        } else {
            throw new NoSuchElementException("Cant't find element " + locator);
        }

    }

    private boolean safeFindElementByHRef(String name) {
        List<WebElement> elements;
        try {
            elements = driver.findElements(By.tagName("a"));
        } catch (NoSuchElementException e) {
            return false;
        }
        for (WebElement element : elements) {
            String href = element.getAttribute("href");
            if (!name.matches("^[a-z]+:.*")) {
                try {
                    name = new URL(new URL(driver.getCurrentUrl()), name).toString();
                } catch (MalformedURLException e) {
                    //
                }
            }
            if (name.equals(href)) {
                this.foundElement = element;
                return true;
            }
        }
        return false;
    }

    private boolean safeFindElement(By id) {
        try {
            foundElement = driver.findElement(id);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

}
