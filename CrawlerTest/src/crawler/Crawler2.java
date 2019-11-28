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
		//�ý��� ����(ȯ�溯�� ������������)
		System.setProperty("webdriver.chrome.driver", "chromedriver/chromedriver77.exe");
		driver = new ChromeDriver();
		jse = (JavascriptExecutor)driver;
		
		List<String> newTab = new ArrayList<String>();
		
		
			//ũ�� ����̹� ����(ũ�� â�� ���� �Ѵ�.)
		WebDriverWait wait = new WebDriverWait(driver, 5);	//�� ����̹��� ��� �ð��� �����Ѵ�.
		
		//driver.findElement()�޼ҵ带 ���� ã���� �ϴ� ��Ұ� �߰ߵ��� ������ NoSuchElementException�� �߻��Ѵ�.
		//�ش� �ڵ带 �ۼ��ϸ� ��Ұ� �߰ߵ� �� ���� 5�ʰ� �����ְ� ��ٸ���.
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		
		//driver.manage().window().maximize();
		//��Ʃ�� ���� �޼ҵ�
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
		
		driver.findElement(By.xpath("//*[@id=\"keyword\"]")).sendKeys("��Ʈ��");
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
			
			//ũ�Ѹ� �۾�
			String src = driver.getPageSource();	//���� �����ִ� ��ü �ҽ��� String���� ��ȯ�Ѵ�.
			Document doc = Jsoup.parse(src);	//Jsoup���� �˾ƺ� �� �ִ� ���� ���·� �Ľ��Ѵ�.
			
			int i = 1;
			while(true) {
				try {
					try {
						wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.cssSelector("#photo_gbPaging"))));
					} catch(TimeoutException te) {
						System.out.println("��ϵ� ��ǰ���� �����ϴ�.");
						break;
					}
					driver.findElement(By.linkText(Integer.toString(i))).sendKeys(Keys.ENTER);
					wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.cssSelector("#photo_comment_list > li"))));
					src = driver.getPageSource();	//���� �����ִ� ��ü �ҽ��� String���� ��ȯ�Ѵ�.
					doc = Jsoup.parse(src);	//Jsoup���� �˾ƺ� �� �ִ� ���� ���·� �Ľ��Ѵ�.
					Elements elements = doc.select("#photo_comment_list > li"); 	//css selector�� �ش��ϴ� html�±� ��ü�� ��ȯ�Ѵ�.
					for (Element element : elements) {	//��ȯ�� ��� ���
						System.out.println(element.text());
					}
					i++;
				} catch (Exception e) {
					if(e instanceof NoSuchElementException) {
						try {
							driver.findElement(By.cssSelector("#photo_gbPaging")).findElement(By.linkText("����")).sendKeys(Keys.ENTER);
						} catch(NoSuchElementException nse) {
							System.out.println("=========================ù��° ��ǰ ��===================================");
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
		//�ǹٲ� �����Ͽ� �������ֱ�
//		newTab.addAll(driver.getWindowHandles());
//		driver.switchTo().window(newTab.get(1));
//		for (int i = 0; i < 15; i++) {
//			driver.findElement(By.cssSelector("body")).sendKeys(Keys.PAGE_DOWN);
//		}
		
		
		//xpath���� �ش��ϴ� ��Ҹ� ã�´�.
//		driver.findElement(By.xpath("//*[@id=\"search\"]")).sendKeys("������");
//		driver.findElement(By.xpath("//*[@id=\"search-icon-legacy\"]")).click();
		
		
		
		
		
		
		

		
		
		
		// body > ytd-app > yt-page-navigation-progress �� �ش��ϴ� ��Ұ� ������ ���� �� ���� ��ٸ���.
//		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("body > ytd-app > yt-page-navigation-progress")));
//		driver.findElement(By.xpath("//*[@id=\"video-title\"]")).click();
		

		
		
	}
	
	
}
