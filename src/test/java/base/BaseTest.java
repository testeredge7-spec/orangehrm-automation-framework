package base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {

	public static WebDriver driver;
	public static Properties prop;

	@BeforeSuite
	public void setup() throws IOException {

		prop = new Properties();

		try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {

			prop.load(fis);
		}

		WebDriverManager.chromedriver().setup();

		driver = new ChromeDriver();

		driver.manage().window().maximize();

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

		driver.get(prop.getProperty("url"));
	}

	@AfterSuite
	public void tearDown() {

		if (driver != null) {

			driver.quit();
		}
	}
}