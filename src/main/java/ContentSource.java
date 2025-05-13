import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * @author wufeng
 * @date 2025/5/9 9:50
 */
public class ContentSource extends Login {

    public static void theme() throws InterruptedException {
        driver.get(domain + "/statics/contentRiskControl/index.html#/contentSource");
        Thread.sleep(1000);
        driver.findElement(By.xpath("//form[@class='t-form TopSearch_form_oHrA1']/div[4]/div[2]/div/div/div/input")).clear();
        driver.findElement(By.xpath("//form[@class='t-form TopSearch_form_oHrA1']/div[4]/div[2]/div/div/div/input")).sendKeys("测试");
        Thread.sleep(100);
        driver.findElement(By.tagName("thead")).click();
        Thread.sleep(500);
        if (isJudgingElement(driver, By.xpath("//tbody[@class='t-table__body']/tr/td[2]"))) {//判断是否有数据
            boolean seted = false;
            List<WebElement> trs = driver.findElements(By.xpath("//tbody[@class='t-table__body']/tr"));//获取操作list
            for (int i = 0; i < trs.size(); i++) {
                if (trs.get(i).findElement(By.xpath("./td[3]/div")).getText().contains("已开通")) {//校验是否是已开通
                    List<WebElement> opers = trs.get(i).findElements(By.xpath("./td[last()]/div/div"));
                    for (int o = 0; o < opers.size(); o++) {
                        if (opers.get(o).getText().contains("策略配置")) {//校验是否是策略配置
                            opers.get(o).click();
                            Thread.sleep(300);
                            driver.findElement(By.xpath("//div[@class='t-tabs__content']/div[1]/div/div[1]/label")).click();//点击第一项的复选框
                            Thread.sleep(300);
                            driver.findElement(By.cssSelector("button.t-button.t-button--variant-base.t-button--theme-primary.t-dialog__confirm")).click();//点击保存
                            seted = true;
                            break;
                        }
                        Thread.sleep(200);
                    }
                }
                Thread.sleep(200);
                if (seted) break;
            }
            if (!seted) System.out.println("~~~~~~~~~~ theme()修改策略配置，执行成功 ~~~~~~~~~~");
        } else System.out.println("没有测试数据");
        Thread.sleep(3000);
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
