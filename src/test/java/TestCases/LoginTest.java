package TestCases;

import Heplers.JsonReader;

import Pages.LoginPage;

import com.aventstack.extentreports.ExtentTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(org.testng.reporters.EmailableReporter2.class)
public class LoginTest extends Baseclass {
    private LoginPage loginPage;


    @BeforeClass
    public void init() {
        JsonReader jsonReader = new JsonReader();

    }

    @Test(priority = 2)
    public void login() throws InterruptedException {
     test = extent.createTest("verify_valid_login");
        extent.flush();

        //open application URL
       // webDriver.get().get(System.getProperty("target.homepage"));
        // Reload the page
       // webDriver.get().navigate().refresh();
        // initialize an object from login page
        loginPage = new LoginPage(webDriver.get());
       //  String title= "[Beta] Ariel 4";
        // if (webDriver.get().getTitle().equals(title)){
       //  System.out.println("Page Loaded Successfully");
          //  }
        // login with username and password provided from test.properties file
       loginPage.login(System.getProperty("account.username"), System.getProperty("account.password"));
        Thread.sleep(2000);
        String titleExp = "Automation Exercise";
        String titleAct = webDriver.get().getTitle();
        Assert.assertEquals(titleExp,titleAct);

    }

    @Test(priority = 1)
    public void login_invalid() throws InterruptedException {

        test = extent.createTest("verify_Invalid_Login");
        extent.flush();
        //open application URL
        webDriver.get().get(System.getProperty("target.homepage"));
        // Reload the page
        webDriver.get().navigate().refresh();
        // initialize an object from login page
        loginPage = new LoginPage(webDriver.get());
        String title= "Automation Exercise";
        if (webDriver.get().getTitle().equals(title)) {
            System.out.println("Page Loaded Successfully");

            // login with username and password provided from test.properties file
           loginPage.login_invalid(System.getProperty("account.username_invalid"),
                    System.getProperty("account.password_invalid"));

            webDriver.get().navigate().refresh();
        }

    }
}
