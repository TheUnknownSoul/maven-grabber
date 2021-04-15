package com.service;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.Setter;
import com.configuration.Configuration;
import com.util.MavenGrabberExecutor;

@Getter
@Setter
public class MavenGrabberService {

	private String uri;
	private int poolSize;
	private HelpFormatter formatter = new HelpFormatter();
	private Options options = new Options();

	static final Logger logger = LogManager.getLogger(MavenGrabberService.class);

	public MavenGrabberService()  {
		try {
			PropertiesConfiguration configuration = Configuration.getConfig();
			this.uri = configuration.getString("uri");
			this.poolSize = configuration.getInt("pool-size");
			logger.info("Maven Grabber Service has been created: ");
		}catch (ConfigurationException configurationException){
			logger.error("no such configuration:" + configurationException.getMessage());
		}
	}

	public void parseArguments(String[] parameters) {
		try {
			Option artifactId = new Option("a", true, "enter artifactId for searching");
			artifactId.setArgs(Option.UNLIMITED_VALUES);
			artifactId.setValueSeparator(',');
			Option groupId = new Option("g", true, "enter groupId for searching");
			groupId.setArgs(Option.UNLIMITED_VALUES);
			Option exit = new Option("exit", false, "to shutdown");
			options.addOption(artifactId);
			options.addOption(groupId);
			options.addOption(exit);

			CommandLineParser parser = new DefaultParser();
			CommandLine cmd;
			cmd = parser.parse(options, parameters);
			if (cmd.hasOption("a")) {
				String[] array = cmd.getOptionValues("a");
				for (String s : array) {
					if (!s.equals("")){
						s = s.trim();
						logger.info("parsing argument:" + s);
						new MavenGrabberExecutor(s, uri, poolSize);
					}
				}
			} else if (cmd.hasOption("g")) {
				String[] array = cmd.getOptionValues("g");
				for (String s : array) {
					if (!s.equals("")){
						s = s.trim();
						logger.info("parsing argument:" + s);
						new MavenGrabberExecutor(s, uri, poolSize);
				}
				}

			} else if (cmd.hasOption("exit")) {
				logger.info("Shutting down");
				System.exit(1);
			} else {
				System.out.println("No such options");
				logger.info("wrong arguments");

			}
		} catch (Exception exception) {
			formatter.printHelp("use one of the following commands",options);
			logger.error("error: " + exception.getMessage());
		}
	}
}



