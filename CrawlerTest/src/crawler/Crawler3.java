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

public class Crawler3 {

	JavascriptExecutor jse;
	WebDriver driver;

	public Crawler3() throws IOException {
		//�ý��� ����(ȯ�溯�� ������������)
		System.setProperty("webdriver.chrome.driver", "chromedriver/chromedriver77.exe");
		driver = new ChromeDriver();
		jse = (JavascriptExecutor)driver;

		List<String> newTab = new ArrayList<String>();


		//ũ�� ����̹� ����(ũ�� â�� ���� �Ѵ�.)
		WebDriverWait wait = new WebDriverWait(driver, 5);   //�� ����̹��� ��� �ð��� �����Ѵ�.

		//driver.findElement()�޼ҵ带 ���� ã���� �ϴ� ��Ұ� �߰ߵ��� ������ NoSuchElementException�� �߻��Ѵ�.
		//�ش� �ڵ带 �ۼ��ϸ� ��Ұ� �߰ߵ� �� ���� 5�ʰ� �����ְ� ��ٸ���.
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);


		//�������� ���� �޼ҵ�
		driver.get("http://www.enuri.com");
		try {
			Thread.sleep(1000);
		} catch(Exception e) {
			e.printStackTrace();
		}


		//�˻�â�� ���� �Է�
		driver.findElement(By.xpath("//*[@id=\"keyword\"]")).sendKeys("노트북");
		//�˻� ��ư Ŭ��
		driver.findElement(By.xpath("//*[@id=\"enuriBi\"]/div/div[2]/div[1]/span/form/a[3]")).click();

		int stopYo = 1; //ũ�ѷ��� ���߰� �ϴ� ����. 1�̸� ����, 0�̸� ����
		int reviewNum = 0; //��ü ���� ���� Ȯ�� ����
		int indiReviewNum = 0; //��ǰ�� ���� ���� Ȯ�� ����
		int cknum = 1; // ��ǰ ���� Ȯ�� ����
		
		while(true) {   
			
			//��Ʈ�� �˻� ��� ������ ũ�Ѹ�
			//�� �������� ��Ÿ���� �˻� ����� 30���̱⿡, 30�� ������.
			
			for(int j = 1; j <=30; j++) {
				try {
					//��ǰ�� Ŭ��
					driver.findElement(By.cssSelector("#listBodyDiv > li.prodItem:nth-child("+ j +") > div.sp_title > strong > a.detailMultiLink.prodName")).click();
				}catch(NoSuchElementException nse) {
					continue;
				}
				//��ǰ�� �ܼ� ���
				System.out.println(driver.findElement(By.cssSelector("#listBodyDiv > li.prodItem:nth-child("+ j +") > div.sp_title > strong > a.detailMultiLink.prodName")).getText());
				//��ǰ���� Ŭ���ϸ� ���� �� ���� ����. ���� ���� �ǿ� �����Ͽ� ����
				newTab.addAll(driver.getWindowHandles());
				driver.switchTo().window(newTab.get(1));

				//ũ�Ѹ� �۾�
				String src = driver.getPageSource();   //���� �����ִ� ��ü �ҽ��� String���� ��ȯ�Ѵ�.
				Document doc = Jsoup.parse(src);   //Jsoup���� �˾ƺ� �� �ִ� ���� ���·� �Ľ��Ѵ�.

				int i = 1;

				//���� ������ ũ�Ѹ�
				while(true) {

					try {
						try {
							//���� �������� ���̰��̼� �ٰ� ��Ÿ�� ������ ������
							wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.cssSelector("#photo_gbPaging"))));
						} catch(TimeoutException te) {
							//Ŭ���� �׺���̼� ��ư�� ������ �극��ũ
							System.out.println("��ϵ� ��ǰ���� �����ϴ�.");
							break;
						}
						//���� ���� ���̰��̼� ��ư Ŭ��(1->2, 2->3 ...)
						driver.findElement(By.linkText(Integer.toString(i))).sendKeys(Keys.ENTER);
						//���� ���� �������� ��Ÿ�� �� ���� ������
						wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.cssSelector("#photo_comment_list > li"))));
						//���� �κ�
						//�����ؾ��� ��: �ش� ���� �������� ���� ������ �ִ� ������ �ƴ� ��, ������ Ƚ����ŭ �ݺ��Ǿ� �ݺ���

						int ck = 0;
						//List<WebElement> list2 = driver.findElements(By.cssSelector("#photo_comment_list > li > div > p"));
						//css class ������� ã��
						List<WebElement> list2 = driver.findElements(By.cssSelector("#photo_comment_list > li > div > p.sub.bbs_click_class"));
						for (WebElement webElement : list2) {
							ck++;
							reviewNum++;//��ü���䰹�� Ȯ��
							indiReviewNum++;//��ǰ�� ���䰹�� Ȯ��
							System.out.println(webElement.getText());
						}

						if(ck<5) break;
						i++;
					} catch (Exception e) {
						if(e instanceof NoSuchElementException) {
							try {
								//���� �׺���̼� ���� '����'��ư Ŭ��
								driver.findElement(By.cssSelector("#photo_gbPaging")).findElement(By.linkText("����")).sendKeys(Keys.ENTER);
							} catch(NoSuchElementException nse) {

								break; 
							}
						} else {
							e.printStackTrace();
						}
					}

				}
				System.out.println();
				System.out.println(cknum+"��° ��ǰ:"+indiReviewNum+"���� �����Դϴ�.");
				System.out.println("������� "+reviewNum+"���� ���並 ã�ҽ��ϴ�.");
				System.out.println();
				System.out.println("========================="+cknum+"��° ��ǰ ��===================================");
				System.out.println();
				
				if(cknum>=50) {
					driver.close();
				}
				
				cknum++;
				indiReviewNum = 0;

				driver.close();
				driver.switchTo().window(newTab.get(0));
				newTab.clear();
				
				
			}
			
			//���� ��ǰ ������ Ŭ��
			driver.findElement(By.cssSelector("#goods_tab01 > div.next_page")).click();
			
			//while�� ��
			
		}



	}


}