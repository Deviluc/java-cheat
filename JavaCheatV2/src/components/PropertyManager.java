package components;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyManager {
	
	private File searchProperties;

	public PropertyManager() throws FileNotFoundException {
		searchProperties = new File(System.getProperties().get("user.dir") + "/config/search.properties");
		
		if (!searchProperties.exists()) {
			throw new FileNotFoundException("Properties-file '" + searchProperties.getAbsolutePath() + "' not found!");
		}
		
	}
	
	public Properties getSearchProperties() throws FileNotFoundException, IOException {
		Properties result = new Properties();
		result.load(new FileInputStream(searchProperties));
		
		return result;
	}
	

}
