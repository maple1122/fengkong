import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wufeng
 * @date 2025/5/6 15:21
 */
public class Login extends CommonMethods {

    static String domain = "http://monitor.pdmiryun.com/";
    static WebDriver driver = initDriver();

    //初始化浏览器
    public static WebDriver initDriver() {
        System.setProperty("webdriver.chrome.driver", "D:\\autotest\\tools\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("hngd.schemes.http.override", "never");
        prefs.put("hngd.schemes.https.override", "never");
        options.setExperimentalOption("prefs", prefs);
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        return driver;
    }

    //登录
    public static WebDriver login(String username, String password) throws InterruptedException {
        String loginUrl = "statics/contentRiskControl/index.html#/work";

        driver.get(domain + loginUrl);
        Thread.sleep(2);

        //校验是否需要登录
        if (isJudgingElement(driver, By.className("submit-btn"))) {
            driver.findElement(By.xpath("//p[@class='input-wrap username-input']/div/div/input")).sendKeys(username);
            driver.findElement(By.xpath("//p[@class='input-wrap password-input']/div/div/input")).sendKeys(password);

            if (isJudgingElement(driver, By.className("slide"))) {
                //手动拖动滑块
                Actions action = new Actions(driver);
                WebElement moveButton = driver.findElement(By.className("slide"));
                //移到滑块元素并悬停
                action.moveToElement(moveButton).clickAndHold(moveButton);
                action.dragAndDropBy(moveButton, 305, 0).perform();
                action.release();
                Thread.sleep(2000);
            } else {
                //验证码，修改前端js为默认验证码
                By canvasImg = By.className("code");
                //验证码输入框
                By yzm = By.xpath("//p[@class='input-wrap code-input']/div/div/input");
                //JS修改验证码变为默认返回值
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("Math.random = function(){return 0}");
                driver.findElement(canvasImg).click();
                Thread.sleep(500);
                //修改后默认验证码AAAA
                driver.findElement(yzm).sendKeys("AAAA");
            }
            Thread.sleep(1000);
            driver.findElement(By.className("submit-btn")).click();//点击登录
        }
        Thread.sleep(2000);

        WebElement userElement=driver.findElement(By.className("user-img"));
        Assert.assertTrue(userElement.isDisplayed());

        return driver;
    }

    //传用户名登录
    public static WebDriver login() throws InterruptedException {
        String username = "testwf1";
        String password = "test1234.";
        driver=login(username, password);
        return driver;
    }

}
