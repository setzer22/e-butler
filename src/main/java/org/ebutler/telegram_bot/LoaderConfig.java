package org.ebutler.telegram_bot;

import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/***
 * 
 * This class load the configuration in .properties file
 * 
 */
public class LoaderConfig {
	private String propFileName;
	private InputStream inputStream;
	
	public LoaderConfig(String propFileName) {
		this.propFileName = propFileName;
	}
	
	public Properties getConfig() throws IOException {
		
		
		try {
			Properties prop = new Properties();
			
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			
			if(inputStream != null) prop.load(inputStream);
			else throw new FileNotFoundException("property file "+ propFileName +" not found");
			
			return prop;
			
		} catch(Exception e) {
			
			System.out.println("Exception: " + e);
			
		} finally {
			
			inputStream.close();
			
		}
		
		return null;
	}
}
