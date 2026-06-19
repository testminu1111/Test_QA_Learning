// java
package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AddToCartPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // locators used in the simple flow
    private By productsHeader = By.xpath("//a[contains(translate(., 'PRODUCTS','products'),'products') or contains(text(),'Products')]");
    private By searchInput = By.xpath("//input[@id='search' or @name='search' or @type='search' or contains(@placeholder,'Search')]");
    private By searchButton = By.xpath("//button[@id='search_button' or contains(text(),'Search')]");
    private By productCards = By.xpath("//div[contains(@class,'product') or contains(@class,'product-item')]");
    private By productNameInCard = By.xpath(".//h2|.//h3|.//div[contains(@class,'product-name') or contains(@class,'name')]");
    private By addToCartBtnInCard = By.xpath(".//button[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'add to cart') or contains(@class,'add-to-cart')]");
    private By continueShoppingBtn = By.xpath("//button[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'continue') and contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'shopping')]");
    private By cartIcon = By.xpath("//a[contains(@class,'cart') or contains(text(),'Cart')]");
    private By cartItemNames = By.xpath("//div[contains(@class,'cart-item')]//h4|//td[@class='cart_description']//h4");

    public AddToCartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    /**
     * Steps:
     * 1) Click on Product tab in header.
     * 2) Click search bar, enter "dresses", click search.
     * 3) For first 5 products: click Add to cart, click Continue shopping, save name.
     * 4) Print saved names.
     * 5) Click Cart in header, verify saved names exist in cart and Assert.
     * Note: If advertisement popup appears, try to close/hide it before actions.
     */
    public List<String> addFirstFiveDressesAndVerify() {
        List<String> savedNames = new ArrayList<>();

        // Popup remover: strip vignette hash and try to remove/hide common overlays
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                    "try{"
                            + " if(window.location.hash && /vignette/i.test(window.location.hash)){ history.replaceState(null,'', window.location.pathname+window.location.search); }"
                            + " Array.from(document.querySelectorAll('button, a')).forEach(function(b){ try{ var t=b.innerText||b.getAttribute('aria-label')||''; if(/close|dismiss|×|ok|got it|accept|agree/i.test(t)) b.click(); }catch(e){} });"
                            + " Array.from(document.querySelectorAll('[id*=\"vignette\"],[class*=\"vignette\"],[class*=\"bottom\"],[class*=\"tray\"],[class*=\"subscribe\"]')).forEach(function(el){ try{ el.style.display='none'; el.style.pointerEvents='none'; }catch(e){} });"
                            + "}catch(e){}");
            Thread.sleep(400);
        } catch (Exception ignored) {}

        // 1) Click on Product tab at header.
        wait.until(ExpectedConditions.elementToBeClickable(productsHeader)).click();

        // small pause to allow possible popup to appear and then attempt to close
        try { Thread.sleep(400); } catch (InterruptedException ignored) {}
        try { closeSmallPopups(); } catch (Exception ignored) {}

        // 2) Click on search bar, enter "dresses", click search.
//        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(searchInput));
//        input.click();
//        input.clear();
        //input.sendKeys("dresses");
//        try {
//            wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
//        } catch (Exception e) {
//            try { input.submit(); } catch (Exception ignored) {}
//        }

        // 3) Wait for product cards and add first 5 to cart saving their names
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(productCards));
        List<WebElement> cards = driver.findElements(productCards);
        int limit = Math.min(5, cards.size());
        for (int i = 0; i < limit; i++) {
            try {
                WebElement card = cards.get(i);
                String name = "";
                try { name = card.findElement(productNameInCard).getText().trim(); } catch (Exception ex) { name = "product-" + (i+1); }
                // click Add to cart inside the card
                try {
                    WebElement addBtn = card.findElement(addToCartBtnInCard);
                    wait.until(ExpectedConditions.elementToBeClickable(addBtn)).click();
                } catch (Exception clicke) {
                    // if direct click fails, attempt click via JS
                    try { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card.findElement(addToCartBtnInCard)); } catch (Exception ignored) {}
                }

                // click Continue shopping (if visible) otherwise attempt to close popup that may block
                try {
                    WebElement cont = wait.until(ExpectedConditions.elementToBeClickable(continueShoppingBtn));
                    cont.click();
                } catch (Exception e) {
                    try { closeSmallPopups(); } catch (Exception ignored) {}
                }

                savedNames.add(name);
                // brief wait before next iteration
                try { Thread.sleep(300); } catch (InterruptedException ignored) {}
            } catch (Exception ignored) {}
        }

        // 4) Print saved names
        System.out.println("Saved product names: " + savedNames);

        // 5) Click on Cart in header
        wait.until(ExpectedConditions.elementToBeClickable(cartIcon)).click();

        // verify saved names are in cart
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(cartItemNames));
        List<String> cartNames = new ArrayList<>();
        for (WebElement e : driver.findElements(cartItemNames)) {
            cartNames.add(e.getText().trim());
        }
        System.out.println("Cart contains: " + cartNames);

        // Assert each saved name exists in cart (case-insensitive contains)
        for (String expected : savedNames) {
            System.out.println("Cexpected " + expected);
            boolean found = false;
            for (String actual : cartNames) {
                System.out.println("actual " + expected);
                if (actual.equalsIgnoreCase(expected) || actual.toLowerCase().contains(expected.toLowerCase())) {
                    found = true;
                    break;
                }
            }
            Assert.assertTrue(found, "Expected product in cart: " + expected);
        }

        return savedNames;
    }

    // small helper kept private and minimal to close simple popups if they appear
    private void closeSmallPopups() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                    "try{ Array.from(document.querySelectorAll('button, a')).forEach(function(b){ try{ var t=b.innerText||b.getAttribute('aria-label')||''; if(/close|dismiss|×|ok|got it|accept|agree/i.test(t)) b.click(); }catch(e){} });"
                            + " Array.from(document.querySelectorAll('[id*=\"vignette\"],[class*=\"vignette\"],[class*=\"subscribe\"],[class*=\"popup\"],[class*=\"cookie\"],[class*=\"trailer\"],[class*=\"drawer\"]')).forEach(function(el){ try{ el.style.display='none'; el.style.pointerEvents='none'; }catch(e){} });"
                            + "}catch(e){}");
            try { Thread.sleep(300); } catch (InterruptedException ignored) {}
        } catch (Exception ignored) {}
    }
}
