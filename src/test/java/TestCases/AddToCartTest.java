// java
// File: `src/test/java/TestCases/AddToCartTest.java`
package TestCases;

import Pages.AddToCartPage;
import Pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class AddToCartTest extends Baseclass {

    private AddToCartPage addToCartPage;
    private LoginPage loginPage;

    @BeforeClass
    public void init() {
        addToCartPage = new AddToCartPage(webDriver.get());
        loginPage = new LoginPage(webDriver.get());
    }

    @Test(priority = 2)
    public void verifyAddToCartAndViewCart() {
        test = extent.createTest("verify_add_to_cart_and_view_cart");
        extent.flush();

        // Use updated single-method flow that adds first dresses and verifies in-cart contents
        List<String> addedProducts = addToCartPage.addFirstFiveDressesAndVerify();

        // Basic assertions: result is not null and at least one product was added
        Assert.assertNotNull(addedProducts, "Returned product list should not be null");
        Assert.assertTrue(!addedProducts.isEmpty(), "No products were added to cart");

        // Optional logging
        System.out.println("Added products: " + addedProducts);
    }
}
