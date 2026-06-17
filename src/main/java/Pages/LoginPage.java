package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    @FindBy(xpath = "//*[@id=\"form\"]/div/div/div[1]/div/form/input[2]")
    private WebElement userNameTxt;

    @FindBy(xpath = "//*[@id=\"form\"]/div/div/div[1]/div/form/input[3]")
    private WebElement passWordTxt;


    @FindBy(xpath = "//*[@id=\"form\"]/div/div/div[1]/div/form/button")
    private WebElement loginBtn;



    private WebDriverWait wait;


    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }



    public void login(String username, String password) throws InterruptedException {

        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOf(userNameTxt),
                ExpectedConditions.visibilityOf(passWordTxt),
                ExpectedConditions.visibilityOf(loginBtn)));


        this.userNameTxt.sendKeys(username);
        this.passWordTxt.sendKeys(password);
        this.loginBtn.click();


    }
    public void login_invalid(String username, String password) throws InterruptedException {

        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOf(userNameTxt),
                ExpectedConditions.visibilityOf(passWordTxt),
                ExpectedConditions.visibilityOf(loginBtn)));


        this.userNameTxt.sendKeys(username);
        this.passWordTxt.sendKeys(password);
        this.loginBtn.click();

    }
        }
