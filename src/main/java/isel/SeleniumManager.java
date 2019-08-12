package isel;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumManager {

	// WebDriver
	private String url;
	private static WebDriver driver;

	// Properties
	public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static final String WEB_DRIVER_PATH = "/Users/yoon/Downloads/selenium/chromedriver";
	public static final HashMap<String, String> MONTHLIST = new HashMap<String, String>();

	public SeleniumManager() {
		MONTHLIST.put("Jan", "01");
		MONTHLIST.put("Feb", "02");
		MONTHLIST.put("Mar", "03");
		MONTHLIST.put("Apr", "04");
		MONTHLIST.put("May", "05");
		MONTHLIST.put("Jun", "06");
		MONTHLIST.put("Jul", "07");
		MONTHLIST.put("Aug", "08");
		MONTHLIST.put("Sep", "10");
		MONTHLIST.put("Oct", "09");
		MONTHLIST.put("Nov", "11");
		MONTHLIST.put("Dec", "12");
	}

	public void init() throws Exception {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.manage().window().setSize(new Dimension(1024, 768));

	}

	@Test
	public String getDate(String url) throws Exception {
		String apacheRegex = "([0-9]){2}\\/([A-Z])\\w+\\/([0-9][0-9])\\s([0-9][0-9]):([0-9][0-9])";
		String mongoDBRegex = "([A-Z|a-z]{3}) ([0-9]{2} 20[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}) ([A-Z|a-z]{2})";
		String created = "";

		driver.get(url);

		WebElement parsing = driver.findElement(By.xpath("//*[@id=\"created-val\"]/time"));

		Thread.sleep(5000);

		if (parsing.getText().contains(apacheRegex)) {
			// 03/Aug/19 00:16
			System.out.println("apache selenium manager 실행 ? ? ? ?");

			String[] date = parsing.getText().split(" ")[0].split("/");
			String time = parsing.getText().split(" ")[1];

			String year = date[2].trim();
			String month = MONTHLIST.get(date[1].trim()).toString();
			String day = date[0].trim();

			created = "20" + year + "-" + month + "-" + day + " " + time;
		} else if (parsing.getText().contains(mongoDBRegex)) {
			// Aug 07 2019 03:07:37 PM GMT+0000
			System.out.println("mongodb selenium manager 실행 ? ? ? ?");

			String[] date = parsing.getText().split(" ")[0].split(" ");
			String[] time = date[3].split(":");

			String year = date[2].trim();
			String month = MONTHLIST.get(date[0].trim()).toString();
			String day = date[1].trim();

			String hour = time[0].trim();
			String min = time[1].trim();

			created = year + "-" + month + "-" + day + " " + hour + ":" + min;
		}

		return created;
	}
}
