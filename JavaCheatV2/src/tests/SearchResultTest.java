package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import models.SearchResult;

public class SearchResultTest {
	
	@Test
	public void testToStringArray() {
		SearchResult res = new SearchResult(Long.decode("0x0F0F0F0F"), 12);
		String[] resToString = res.toStringArray();
		
		System.out.println("Address: " + resToString[0] + ";\tValue: " + resToString[1]);
		
		Assert.assertEquals(resToString[0], "F0F0F0F");
		Assert.assertEquals(resToString[1], "12");
		
	}

}
