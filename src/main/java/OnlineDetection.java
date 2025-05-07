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
    public static void checkText() throws InterruptedException {
        driver.get(domain + "/statics/contentRiskControl/index.html#/onlineDetection");
        Thread.sleep(2000);
        selectTab(1);
        driver.findElement(By.xpath("//div[@class='w-e-text-container']/div[3]/div/p/span/span/span")).sendKeys("现在，随着教育的发展，很多⼈都可以得到幼⼉园-⼩学-中学-⼤学这⼏个阶段的教育。——autotest文稿测试（" + System.currentTimeMillis() + "）");
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("button.t-button.t-button--variant-base.t-button--theme-primary.BodyPanel_submit_btn_dmUIE")).click();//点击开始检测
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("button.t-button.t-button--variant-base.t-button--theme-primary.CheckResultDialog_btn_LVZTA")).click();
        Thread.sleep(3000);
    }

    //查图片
    public static void checkImg() throws InterruptedException {
        driver.get(domain + "/statics/contentRiskControl/index.html#/onlineDetection");
        Thread.sleep(2000);
        selectTab(2);
        driver.findElement(By.xpath("//div[@class='LinkAndUpload_wrap_ujoLH']/div/label[2]")).click();//点击图片链接
        Thread.sleep(200);
        driver.findElement(By.xpath("//div[@class='list']/div/div[2]/div/div/div/input")).sendKeys("http://nisptools.nos.netease.com/yidun-tmp-04c56e5f50c847b347c0073495d95c9e.jpg");//录入图片链接
        Thread.sleep(200);
        driver.findElement(By.cssSelector("button.t-button.t-button--variant-base.t-button--theme-primary.BodyPanel_submit_btn_dmUIE")).click();//点击开始检测
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("button.t-button.t-button--variant-base.t-button--theme-primary.CheckResultDialog_btn_LVZTA")).click();
        Thread.sleep(3000);
    }

    //查音频
    public static void checkAudio() throws InterruptedException {
        driver.get(domain + "/statics/contentRiskControl/index.html#/onlineDetection");
        Thread.sleep(2000);
        selectTab(3);
        driver.findElement(By.xpath("//div[@class='LinkAndUpload_wrap_ujoLH']/div/label[2]")).click();//点击音频链接
        Thread.sleep(200);
        driver.findElement(By.xpath("//div[@class='list']/div/div[2]/div/div/div/input")).sendKeys("http://nisptools.nos.netease.com/68e1d9d922f01e69d3b8a9baa7a73269.mp3");//录入音频链接
        Thread.sleep(200);
        driver.findElement(By.cssSelector("button.t-button.t-button--variant-base.t-button--theme-primary.BodyPanel_submit_btn_dmUIE")).click();//点击开始检测
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("button.t-button.t-button--variant-base.t-button--theme-primary.CheckResultDialog_btn_LVZTA")).click();
        Thread.sleep(3000);
    }

    //查视频
    public static void checkVideo() throws InterruptedException {
        driver.get(domain + "/statics/contentRiskControl/index.html#/onlineDetection");
        Thread.sleep(2000);
        selectTab(4);
        driver.findElement(By.xpath("//div[@class='LinkAndUpload_wrap_ujoLH']/div/label[2]")).click();//点击视频链接
        Thread.sleep(200);
        driver.findElement(By.xpath("//div[@class='list']/div/div[2]/div/div/div/input")).sendKeys("http://nisptools.nos.netease.com/930cdccb613a9462d7f8fc9be485ee2e.mp4");//录入视频链接
        Thread.sleep(200);
        driver.findElement(By.cssSelector("button.t-button.t-button--variant-base.t-button--theme-primary.BodyPanel_submit_btn_dmUIE")).click();//点击开始检测
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("button.t-button.t-button--variant-base.t-button--theme-primary.CheckResultDialog_btn_LVZTA")).click();
        Thread.sleep(3000);
    }

    //批量检测
    public void checkBatch() throws InterruptedException {
        selectTab(5);

    }

    //tab切换
    private static void selectTab(int type) throws InterruptedException {
        List<WebElement> tabs = driver.findElements(By.xpath("//div[@class='t-tabs__nav-scroll']/div/div"));
        if (type > 0 && type < 6) {
            tabs.get(type).findElement(By.xpath("./div")).click();//点击tab
        }
        Thread.sleep(500);
    }

    //初始化
    static {
        try {
            driver = login();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
