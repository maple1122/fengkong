import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * @author wufeng
 * @date 2025/5/6 16:28
 */
public class OnlineDetection extends Login {

    static WebDriver driver;

    //查文稿
    public void checkText() throws InterruptedException {
        selectTab(1);
        driver.findElement(By.xpath("//div[@class='w-e-text-container']/div[3]/div/p/span/span/span")).sendKeys("现在，随着教育的发展，很多⼈都可以得到幼⼉园-⼩学-中学-⼤学这⼏个阶段的教育。——autotest文稿测试（" + System.currentTimeMillis() + "）");
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("button.t-button.t-button--variant-base.t-button--theme-primary.BodyPanel_submit_btn_dmUIE")).click();
        Thread.sleep(3000);
    }

    //查图片
    public void checkImg() throws InterruptedException {
        selectTab(2);
        driver.findElement(By.xpath("//div[@class='LinkAndUpload_wrap_ujoLH']/div/label[2]")).click();//点击图片链接
        Thread.sleep(200);
        
    }

    //查音频
    public void checkAudio() {

    }

    //查视频
    public void checkVideo() {

    }

    //批量检测
    public void checkBatch() {

    }

    //tab切换
    private void selectTab(int type) throws InterruptedException {
        List<WebElement> tabs = driver.findElements(By.xpath("//div[@class='t-tabs__nav-scroll']/div/div"));
        if (type > 1 && type < 5) {
            tabs.get(type - 1).click();//点击tab
        }
        Thread.sleep(500);
    }
}
