package tests;

import java.io.IOException;

import org.testng.annotations.Test;

import components.PropertyManager;

public class PropertyManagerTest {

	public PropertyManagerTest() {
		// TODO Auto-generated constructor stub
	}
	
	@Test
	public void testSearchProperties() throws IOException {
		System.out.println(new PropertyManager().getSearchProperties().getProperty("buffer.size"));
	}

}
