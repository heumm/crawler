package crawler;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		//Crawler c = new Crawler();
		Runtime.getRuntime().exec("taskkill /F /IM chromedriver77.exe /T");
		//Crawler2 c = new Crawler2();
		Crawler3 c = new Crawler3();
		
	}

}
