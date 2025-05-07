import junit.framework.TestCase;
import org.testng.annotations.*;

import java.lang.reflect.Method;

/**
 * @author wufeng
 * @date 2025/5/7 10:08
 */
public class OnlineDetectionTest extends TestCase {

    @Test(priority = 1)//文本检测
    public void testCheckText() throws InterruptedException {
        OnlineDetection.checkText();
    }

    @Test(priority = 2)//图片检测
    public void testCheckImg() throws InterruptedException {
        OnlineDetection.checkImg();
    }

    @Test(priority = 3)//音频检测
    public void testCheckAudio() throws InterruptedException {
        OnlineDetection.checkAudio();
    }

    @Test(priority = 4)//视频检测
    public void testCheckVideo() throws InterruptedException {
        OnlineDetection.checkVideo();
    }

    @BeforeMethod
    public void beforeTest(Method method) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> Test case:" + method.getName());
    }

    @AfterMethod
    public void afterTest(Method method) {
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<< Test End!\n");
    }
}