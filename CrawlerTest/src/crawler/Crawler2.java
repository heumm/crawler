package crawler;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Crawler2 {
	
	JavascriptExecutor jse;
	WebDriver driver;
	
	public Crawler2() throws IOException {
		//시스템 설정(환경변수 설정같은느낌)
		System.setProperty("webdriver.chrome.driver", "chromedriver/chromedriver77.exe");
		driver = new ChromeDriver();
		jse = (JavascriptExecutor)driver;
		
		List<String> newTab = new ArrayList<String>();
		
		
			//크롬 드라이버 생성(크롬 창을 열게 한다.)
		WebDriverWait wait = new WebDriverWait(driver, 5);	//웹 드라이버의 대기 시간을 설정한다.
		
		//driver.findElement()메소드를 통해 찾고자 하는 요소가 발견되지 않으면 NoSuchElementException이 발생한다.
		//해당 코드를 작성하면 요소가 발견될 때 까지 5초간 관용있게 기다린다.
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		
		//driver.manage().window().maximize();
		//유튜브 접속 메소드
		driver.get("http://www.enuri.com");
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
		
		driver.findElement(By.xpath("//*[@id=\"keyword\"]")).sendKeys("노트북");
		driver.findElement(By.xpath("//*[@id=\"enuriBi\"]/div/div[2]/div[1]/span/form/a[3]")).click();
		
		
//		#listBodyDiv > li.prodItem > div.sp_title > strong > a.detailMultiLink.prodName
//		#listBodyDiv > li.prodItem:nth-child() > div.sp_title > strong > a.detailMultiLink.prodName
		
//		driver.findElement(By.xpath("//*[@id=\"modelno_35176336\"]/div[1]/strong/a[1]")).click();
	while(true) {	
		
		for(int j = 1; j <=30; j++) {
			try {
				driver.findElement(By.cssSelector("#listBodyDiv > li.prodItem:nth-child("+ j +") > div.sp_title > strong > a.detailMultiLink.prodName")).click();
			}catch(NoSuchElementException nse) {
				continue;
			}
			System.out.println(driver.findElement(By.cssSelector("#listBodyDiv > li.prodItem:nth-child("+ j +") > div.sp_title > strong > a.detailMultiLink.prodName")).getText());
			newTab.addAll(driver.getWindowHandles());
			driver.switchTo().window(newTab.get(1));
			
			//크롤링 작업
			String src = driver.getPageSource();	//현재 열려있는 전체 소스를 String으로 반환한다.
			Document doc = Jsoup.parse(src);	//Jsoup에서 알아볼 수 있는 문서 형태로 파싱한다.
			
			int i = 1;
			while(true) {
				try {
					try {
						wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.cssSelector("#photo_gbPaging"))));
					} catch(TimeoutException te) {
						System.out.println("등록된 상품평이 없습니다.");
						break;
					}
					driver.findElement(By.linkText(Integer.toString(i))).sendKeys(Keys.ENTER);
					wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.cssSelector("#photo_comment_list > li"))));
					src = driver.getPageSource();	//현재 열려있는 전체 소스를 String으로 반환한다.
					doc = Jsoup.parse(src);	//Jsoup에서 알아볼 수 있는 문서 형태로 파싱한다.
					Elements elements = doc.select("#photo_comment_list > li"); 	//css selector에 해당하는 html태그 전체를 반환한다.
					for (Element element : elements) {	//반환된 요소 출력
						System.out.println(element.text());
					}
					i++;
				} catch (Exception e) {
					if(e instanceof NoSuchElementException) {
						try {
							driver.findElement(By.cssSelector("#photo_gbPaging")).findElement(By.linkText("다음")).sendKeys(Keys.ENTER);
						} catch(NoSuchElementException nse) {
							System.out.println("=========================첫번째 제품 끝===================================");
							break;
						}
					} else {
						e.printStackTrace();
					}
				}
			}
			
			
			driver.close();
			driver.switchTo().window(newTab.get(0));
			newTab.clear();
		}
		
		driver.findElement(By.cssSelector("#goods_tab01 > div.next_page")).click();
		
		
		
	}
		//탭바뀔때 셀레니움 지정해주기
//		newTab.addAll(driver.getWindowHandles());
//		driver.switchTo().window(newTab.get(1));
//		for (int i = 0; i < 15; i++) {
//			driver.findElement(By.cssSelector("body")).sendKeys(Keys.PAGE_DOWN);
//		}
		
		
		//xpath값에 해당하는 요소를 찾는다.
//		driver.findElement(By.xpath("//*[@id=\"search\"]")).sendKeys("아이폰");
//		driver.findElement(By.xpath("//*[@id=\"search-icon-legacy\"]")).click();
		
		
		
		
		
		
		

		
		
		
		// body > ytd-app > yt-page-navigation-progress 에 해당하는 요소가 보이지 않을 때 까지 기다린다.
//		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("body > ytd-app > yt-page-navigation-progress")));
//		driver.findElement(By.xpath("//*[@id=\"video-title\"]")).click();
		

		
		
	}
	
	
}
