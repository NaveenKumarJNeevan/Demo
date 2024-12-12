package demo;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;


public class Demo {
	
	public static String checkBoxXpath = "/html/body/app-root/section/app-todo-list/main/ul/app-todo-item[1]/li/div/input";
	public static String itemsXpath = "/html/body/app-root/section/app-todo-list/main/ul/app-todo-item[1]";
	public static String checkBoxtextXpath = "/html/body/app-root/section/app-todo-list/main/ul/app-todo-item[1]/li/div/label";
	public static String deleteButton = "/html/body/app-root/section/app-todo-list/main/ul/app-todo-item[1]/li/div/button";
	public static String filterActive = "/html/body/app-root/section/app-todo-footer/footer/ul/li[2]/a";
	public static String filterCompleted = "/html/body/app-root/section/app-todo-footer/footer/ul/li[3]/a";
	
    public static void validateAdditems(List<String> items, WebDriver driver) {

      	 
    	List<WebElement> allOptions = driver.findElements(By.className("todo-list"));

    	WebElement element = driver.findElement(By.className("todo-count"));
    	
    	//Validate the Visible elements
    	
    	for(int i = 0; i < items.size(); i++) {
    		WebElement listElement = driver.findElement(By.xpath(itemsXpath.replaceAll("1", Integer.toString(i+1))));
    		Assert.assertEquals(listElement.getText(), items.get(i));
    		
    	}
    	
    	WebElement innerElement = element.findElement(By.tagName("strong"));
    	
    	String count = innerElement.getText();
    	Assert.assertEquals(count, Integer.toString(items.size()));
    	
    }
    
	private static void markItemCompleted(List<String> items, WebDriver driver, int count) {
		
		for(int i = 0; i < count; i++) {
		WebElement element = driver.findElement(By.xpath(checkBoxXpath.replaceAll("1", Integer.toString(i+1))));
		element.click();

		WebElement elementText = driver.findElement(By.xpath(checkBoxtextXpath.replaceAll("1", Integer.toString(i+1))));
		Assert.assertEquals(elementText.getText(), items.get(i));
		Assert.assertEquals(elementText.getCssValue("text-decoration-line") , "line-through");
		}
		
		WebElement element = driver.findElement(By.className("todo-count"));
		
        WebElement innerElement = element.findElement(By.tagName("strong"));
    	
    	String counts = innerElement.getText();
    	Assert.assertEquals(counts, Integer.toString(count));
		
		
	}
	
	private static void deleteItem(List<String> items, WebDriver driver, int itemNumber) throws InterruptedException {
		// TODO Auto-generated method stub
		
		String Xpath = "/html/body/app-root/section/app-todo-list/main/ul/app-todo-item[1]/li/div";
		Xpath.replace("1", Integer.toString(itemNumber));
		WebElement element = driver.findElement(By.xpath(Xpath));
		
		//Instantiating Actions class
		Actions actions = new Actions(driver);

		//Hovering on main menu
		actions.moveToElement(element);

		// Locating the element from Sub Menu
		WebElement subMenu = driver.findElement(By.xpath(deleteButton.replace("1",Integer.toString(itemNumber))));

		//To mouseover on sub menu
		actions.moveToElement(subMenu);

		//build()- used to compile all the actions into a single step 
		actions.click().build().perform();
		element.click();
		
		List<String> updatedItem = items;
		updatedItem.remove(itemNumber-1);
		
		
		for(int i = 0; i < updatedItem.size(); i++) {
			WebElement elementText = driver.findElement(By.xpath(checkBoxtextXpath.replaceAll("1", Integer.toString(i+1))));
			Assert.assertEquals(elementText.getText(), items.get(i));
		}
		
        WebElement elementList = driver.findElement(By.className("todo-count"));
		
        WebElement innerElement = elementList.findElement(By.tagName("strong"));
    	
    	String counts = innerElement.getText();
    	Assert.assertEquals(counts, Integer.toString(updatedItem.size()));
		
	}
    
    public static void addItems(List<String> items, WebDriver driver) {
    	for(int i = 0; i < items.size(); i++) {
       	 WebElement element = driver.findElement(By.className("new-todo"));
       	 element.sendKeys(items.get(i));
       	 element.sendKeys(Keys.RETURN);
       	}
    	
        validateAdditems(items, driver);
    	
    }

    
	private static void VerifyFilters(List<String> items, WebDriver driver, String filter) {
		WebElement element = null;
		// TODO Auto-generated method stub
		if(filter.equals("active")) {
		 element = driver.findElement(By.xpath(filterActive));
		 element.click();
		 for(int i = 0; i < 2; i++) {
		        WebElement elementText = driver.findElement(By.xpath(checkBoxtextXpath.replaceAll("1", Integer.toString(i+1))));
				Assert.assertEquals(elementText.getText(), items.get(i+2));
		     }
		 
		} else if(filter.equals("completed")){
	     element = driver.findElement(By.xpath(filterCompleted));
		 element.click();
	     for(int i = 0; i < 2; i++) {
	        WebElement elementText = driver.findElement(By.xpath(checkBoxtextXpath.replaceAll("1", Integer.toString(i+1))));
			Assert.assertEquals(elementText.getText(), items.get(i));
			Assert.assertEquals(elementText.getCssValue("text-decoration-line") , "line-through");
	     }

		} else {
			Assert.fail("No Active Filter Available");
		}
		
		
		
	}

    @SuppressWarnings("null")
	public static void main(String[] args) throws InterruptedException {
    	
		System.setProperty("webdriver.chrome.driver","C:\\Users\\Test-User.LMT-L117\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
    	
        String baseUrl = "https://todomvc.com/examples/angular/dist/browser/#/";
        driver.get(baseUrl);
        
        
        List<String> items = new ArrayList();
        items.add("First Item");
        items.add("Second Item");
        items.add("Third Item");
        items.add("Fourth Item");
        
        addItems(items, driver);
        
        markItemCompleted(items, driver, 2);
        
        deleteItem(items, driver, 3);
        System.out.println("Test");
        VerifyFilters(items, driver, "completed");
        VerifyFilters(items, driver, "active");
        
        

        driver.wait(3000);
        driver.close();
    }






    


}