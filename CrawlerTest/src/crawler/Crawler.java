package crawler;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Crawler {
	
	JavascriptExecutor jse;
	WebDriver driver;
	
	public Crawler() throws IOException {
		//시스템 설정(환경변수 설정같은느낌)
		System.setProperty("webdriver.chrome.driver", "chromedriver/chromedriver77.exe");
		driver = new ChromeDriver();
		jse = (JavascriptExecutor)driver;
		
		
		
		
			//크롬 드라이버 생성(크롬 창을 열게 한다.)
		WebDriverWait wait = new WebDriverWait(driver, 10);	//웹 드라이버의 대기 시간을 설정한다.
		
		//driver.findElement()메소드를 통해 찾고자 하는 요소가 발견되지 않으면 NoSuchElementException이 발생한다.
		//해당 코드를 작성하면 요소가 발견될 때 까지 5초간 관용있게 기다린다.
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		//유튜브 접속 메소드
		driver.get("https://www.youtube.com/watch?v=XlH3PyzI8Og");
		try {
		Thread.sleep(1000);
		} catch(Exception e) {
			e.printStackTrace();
		}
//		System.out.println(driver.manage().window().getPosition().y);
//	    for(int i=0; i<1000; i++) {
//	        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,10)", "");
//	    }
	    
//	    System.out.println(driver.manage().window().getPosition().y);
		//#spinnerContainer > div.spinner-layer.layer-4.style-scope.paper-spinner > div.circle-clipper.left.style-scope.paper-spinner > div
		while(true) {
			String src = driver.getPageSource();
			for (int i = 0; i < 10; i++) {
				driver.findElement(By.cssSelector("body")).sendKeys(Keys.PAGE_DOWN);
//				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#spinnerContainer > div.spinner-layer.layer-4.style-scope.paper-spinner > div.circle-clipper.left.style-scope.paper-spinner > div")));
			}
			String temp = driver.getPageSource();
			if(src.equals(temp)) {
				System.out.println("끝");
				break;
			}
		}
		List<WebElement> list = driver.findElements(By.xpath("//*[@id=\"content-text\"]"));
		for (WebElement webElement : list) {
			System.out.println(webElement.getText());
		}
		
		
		//xpath값에 해당하는 요소를 찾는다.
//		driver.findElement(By.xpath("//*[@id=\"search\"]")).sendKeys("아이폰");
//		driver.findElement(By.xpath("//*[@id=\"search-icon-legacy\"]")).click();
		
		
//		String src = driver.getPageSource();	//현재 열려있는 전체 소스를 String으로 반환한다.
//		Document doc = Jsoup.parse(src);	//Jsoup에서 알아볼 수 있는 문서 형태로 파싱한다.
//		Elements elements = doc.select("#video-title"); 	//css selector에 해당하는 html태그 전체를 반환한다.
//		for (Element element : elements) {	//반환된 요소 출력
//			System.out.println(element.text());
//		}
		
		// body > ytd-app > yt-page-navigation-progress 에 해당하는 요소가 보이지 않을 때 까지 기다린다.
//		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("body > ytd-app > yt-page-navigation-progress")));
//		driver.findElement(By.xpath("//*[@id=\"video-title\"]")).click();
		

		
		
	}
	
	
}
