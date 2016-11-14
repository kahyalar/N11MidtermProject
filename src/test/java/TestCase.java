import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestCase {
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    //Login Info
    String email = "pandafirarda@gmail.com";
    String password = "n11testcase";

    //XPathes
    String kitapUstMenu = "//nav[@class=\"catMenu\"]/ul/li[8]";
    String kitapAltMenu = "//li[@class=\"mainCat\"][1]";


    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Furkan\\Downloads\\chromedriver.exe");
        driver = new ChromeDriver();
        baseUrl = "http://www.n11.com";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    public void facebookLogin(String mail, String pass){
        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("pass")).click();
        driver.findElement(By.id("pass")).clear();
        driver.findElement(By.id("pass")).sendKeys(password);
        driver.findElement(By.id("u_0_2")).click();
    }

    public void loginOperations(){
        Set<String> set=driver.getWindowHandles();
        Iterator<String> facebookPopUp = set.iterator();
        String parent=facebookPopUp.next();
        String child=facebookPopUp.next();
        driver.switchTo().window(child);
        facebookLogin(email, password);
        driver.switchTo().window(parent);
    }

    @Test
    public void testCase() throws Exception {

        driver.get(baseUrl + "/");
        driver.findElement(By.className("sgm-notification-close")).click();
        driver.findElement(By.className("btnSignIn")).click();
        driver.findElement(By.className("facebookBtn")).click();
        loginOperations();
        Thread.sleep(1500);
        assertEquals("n11.com - Alışverişin Uğurlu Adresi", driver.getTitle());
        driver.findElement(By.xpath(kitapUstMenu)).click();
        //driver.findElement(By.className("fancybox-close")).click();
        driver.findElement(By.xpath(kitapAltMenu)).click();
        assertEquals("Kitap", driver.findElement(By.cssSelector("h1")).getText());
        driver.findElement(By.linkText("Tüm Liste")).click();
        assertEquals("Yazarlar - Türk ve Yabancı Yazarlar - n11.com", driver.getTitle());
        driver.close();
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
