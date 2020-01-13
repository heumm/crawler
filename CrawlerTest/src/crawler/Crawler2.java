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
	      WebDriverWait wait = new WebDriverWait(driver, 5);   //웹 드라이버의 대기 시간을 설정한다.
	      
	      //driver.findElement()메소드를 통해 찾고자 하는 요소가 발견되지 않으면 NoSuchElementException이 발생한다.
	      //해당 코드를 작성하면 요소가 발견될 때 까지 5초간 관용있게 기다린다.
	      driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	      
	      //driver.manage().window().maximize();
	      //웹페이지 접속 메소드
	      driver.get("http://www.enuri.com");
	      try {
	      Thread.sleep(1000);
	      } catch(Exception e) {
	         e.printStackTrace();
	      }
	//      System.out.println(driver.manage().window().getPosition().y);
	//       for(int i=0; i<1000; i++) {
	//           ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,10)", "");
	//       }
	       
	//       System.out.println(driver.manage().window().getPosition().y);
	      //#spinnerContainer > div.spinner-layer.layer-4.style-scope.paper-spinner > div.circle-clipper.left.style-scope.paper-spinner > div
	      
	      //검색창에 문자 입력
	      driver.findElement(By.xpath("//*[@id=\"keyword\"]")).sendKeys("노트북");
	      //검색 버튼 클릭
	      driver.findElement(By.xpath("//*[@id=\"enuriBi\"]/div/div[2]/div[1]/span/form/a[3]")).click();
	      
	      
	//      #listBodyDiv > li.prodItem > div.sp_title > strong > a.detailMultiLink.prodName
	//      #listBodyDiv > li.prodItem:nth-child() > div.sp_title > strong > a.detailMultiLink.prodName
	      
	//      driver.findElement(By.xpath("//*[@id=\"modelno_35176336\"]/div[1]/strong/a[1]")).click();
	   int n = 0;
	   while(true) {   
	      //노트북 검색 결과 페이지 크롤링
		  //한 페이지에 나타나는 검색 결과가 30개이기에, 30번 돌리자.
	      for(int j = 1; j <=30; j++) {
	         try {
	        	//제품명 클릭
	            driver.findElement(By.cssSelector("#listBodyDiv > li.prodItem:nth-child("+ j +") > div.sp_title > strong > a.detailMultiLink.prodName")).click();
	         }catch(NoSuchElementException nse) {
	            continue;
	         }
	         //제품명 콘솔 출력
	         System.out.println(driver.findElement(By.cssSelector("#listBodyDiv > li.prodItem:nth-child("+ j +") > div.sp_title > strong > a.detailMultiLink.prodName")).getText());
	         //제품명을 클릭하면 구글 새 탭이 열림. 새로 열린 탭에 셀레니움 적용
	         newTab.addAll(driver.getWindowHandles());
	         driver.switchTo().window(newTab.get(1));
	         
	         //크롤링 작업
	         String src = driver.getPageSource();   //현재 열려있는 전체 소스를 String으로 반환한다.
	         Document doc = Jsoup.parse(src);   //Jsoup에서 알아볼 수 있는 문서 형태로 파싱한다.
	         
	         int i = 1;
	         
	         //리뷰 페이지 크롤링
	         while(true) {
	            try {
	               try {
	            	 //리뷰 페이지의 네이게이션 바가 나타날 때까지 웨이팅
	            	 wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.cssSelector("#photo_gbPaging"))));
	               } catch(TimeoutException te) {
	            	  //클릭할 네비게이션 버튼이 없으면 브레이크
	                  System.out.println("등록된 상품평이 없습니다.");
	                  break;
	               }
	               //다음 리뷰 네이게이션 버튼 클릭(1->2, 2->3 ...)
	               driver.findElement(By.linkText(Integer.toString(i))).sendKeys(Keys.ENTER);
	               //다음 리뷰 페이지가 나타날 때 까지 웨이팅
	               wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.cssSelector("#photo_comment_list > li"))));
	               //수정 부분
	               //수정해야할 점: 해당 리뷰 페이지의 리뷰 갯수가 최대 갯수가 아닐 시, 무작위 횟수만큼 반복되어 반복됨
	               	
	               src = driver.getPageSource();   //현재 열려있는 전체 소스를 String으로 반환한다.
	               doc = Jsoup.parse(src);   //Jsoup에서 알아볼 수 있는 문서 형태로 파싱한다.
	               Elements elements = doc.select("#photo_comment_list > li");    //css selector에 해당하는 html태그 전체를 반환한다.
	              
	              
	               int ck = 0;
	               
	               
	               for (Element element : elements) {   //반환된 요소 출력
	            	   System.out.println(element.text());
	            	   ck++;
	                  //조건문 걸기. 만약 리뷰의 갯수가 5개보다 작을 시, 해당 리뷰갯수만큼만 출력하고 브레이크
	                  //ul의 크기를 재서 li 갯수만큼?
	            	   
	               }
	               
	               if(ck<5) break;
	               /*src = driver.getPageSource();   //현재 열려있는 전체 소스를 String으로 반환한다.
	               doc = Jsoup.parse(src);   //Jsoup에서 알아볼 수 있는 문서 형태로 파싱한다.
	               Elements elements = doc.select("#photo_comment_list > li");    //css selector에 해당하는 html태그 전체를 반환한다.
	               for (Element element : elements) {   //반환된 요소 출력
	                  System.out.println(element.text());
	               }*/
	               i++;
	            } catch (Exception e) {
	               if(e instanceof NoSuchElementException) {
	                  try {
	                	 //리뷰 네비게이션 바의 '다음'버튼 클릭
	                     driver.findElement(By.cssSelector("#photo_gbPaging")).findElement(By.linkText("다음")).sendKeys(Keys.ENTER);
	                  } catch(NoSuchElementException nse) {
	                	 System.out.println();
	                     System.out.println("=========================제품 끝===================================");
	                     System.out.println();
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
	      //다음 제품 페이지 클릭
	      driver.findElement(By.cssSelector("#goods_tab01 > div.next_page")).click();
	   //while문 끝
	      
	   }
      
   
      
   }
   
   
}