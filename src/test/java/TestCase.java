import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestCase {
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    //Login Info
    private String email = "pandafirarda@gmail.com";
    private String password = "n11testcase";

    //XPathes
    private String bookTopMenu = "//nav[@class=\"catMenu\"]/ul/li[8]";
    private String bookLowMenu = "//li[@class=\"mainCat\"][1]";
    private String authors = "//div[@id='authorsList']/div/ul/li/a";
    private String section09 = "//span[@class='alphabetSearch'][1]";
    private String lastAuthor = "//div[@id='authorsList']/div[4]/ul/li[19]/a";
    private String buttonSecondPage = "//div[@class='pagination']/a[2]";


    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Furkan\\Downloads\\chromedriver.exe");
        driver = new ChromeDriver();
        baseUrl = "http://www.n11.com";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    private void facebookLogin(String mail, String pass){
        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("pass")).click();
        driver.findElement(By.id("pass")).clear();
        driver.findElement(By.id("pass")).sendKeys(password);
        driver.findElement(By.id("u_0_2")).click();
    }

    //First 5 line of code, found from internet. Stands for handling pop-up login operation.
    private void loginOperations(){
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
        Thread.sleep(1500); //Waits 1.5 seconds for completing all login operations and page loads.
        assertEquals("n11.com - Alışverişin Uğurlu Adresi", driver.getTitle());
        driver.findElement(By.xpath(bookTopMenu)).click();
        driver.findElement(By.xpath(bookLowMenu)).click();
        assertEquals("Kitap", driver.findElement(By.cssSelector("h1")).getText());
        driver.findElement(By.linkText("Tüm Liste")).click();
        assertEquals("Yazarlar - Türk ve Yabancı Yazarlar - n11.com", driver.getTitle());
        driver.findElement(By.xpath(section09)).click();//Driver click's to 0-9 section. Beacuse we don't need them.

        for (int i= 1; i <= 31; i++) {
            driver.findElement(By.xpath(String.format("//span[@class='alphabetSearch'][%d]",i))).click();
            List<WebElement> authorList = driver.findElements(By.xpath(authors));
            if(authorList.size() == 80){
                String authorNameLastIndex = driver.findElement(By.xpath(lastAuthor)).getAttribute("title");
                driver.findElement(By.xpath(buttonSecondPage)).click();
                for (int k = 1; k <= 4 ; k++) {
                    List<WebElement> authorListSecondPage = driver.findElements(By.xpath(String.format("//div[@id='authorsList']/div[%d]/ul/li/a",k)));
                    for (int j = 1; j <= authorListSecondPage.size(); j++) { // Checks the second page for comparing duplicate name.
                        String authorNameSecondPage = driver.findElement(By.xpath(String.format("//div[@id='authorsList']/div/ul/li[%d]/a",j))).getAttribute("title");
                        assertFalse(authorNameLastIndex.equals(authorNameSecondPage));
                    }
                }
            }
            else{
                System.out.println(String.format("Assertion failed at index: %d", i));
            }
        }
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
