import junit.framework.TestCase;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * @author wufeng
 * @date 2025/5/12 15:35
 */
public class ContentSourceTest extends TestCase {

    @Test
    public void testTheme() throws InterruptedException {
        ContentSource.theme();
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