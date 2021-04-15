package com.configuration;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Configuration {

	private static final String CONFIG_FILE = "env.properties";


	public static PropertiesConfiguration getConfig() throws ConfigurationException {
		PropertiesConfiguration config = new PropertiesConfiguration();
		config.load(CONFIG_FILE);
		return config;
	}

}
