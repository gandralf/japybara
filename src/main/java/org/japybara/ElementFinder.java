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
                safeFindElementByLabel(locator) ||
                safeFindElementByHRef(locator)) {
            return foundElement;
        } else {
            throw new NoSuchElementException("Cant't find element " + locator);
        }

    }

    private boolean safeFindElementByHRef(String name) {
        List<WebElement> elements = safeFindElementsFindBy(By.tagName("a"));
        if (elements != null) {
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
        }

        return false;
    }

    private boolean safeFindElementByLabel(String labelValue) {
        List<WebElement> elements = safeFindElementsFindBy(By.tagName("label"));
        if (elements != null) {
            for (WebElement element : elements) {
                if (labelValue.equals(element.getText().trim())) {
                    String targetId = element.getAttribute("for");
                    return safeFindElement(By.id(targetId));
                }
            }
        }

        return false;
    }

    private boolean safeFindElement(By by) {
        try {
            foundElement = driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private List<WebElement> safeFindElementsFindBy(By by) {
        try {
            return driver.findElements(by);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

}
