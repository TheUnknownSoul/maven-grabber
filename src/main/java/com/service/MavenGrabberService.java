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

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

@Getter
@Setter
public class MavenGrabberService {

    private HelpFormatter formatter = new HelpFormatter();
    private Options options = new Options();
    private static final String DESCRIPTION_FOR_ARTIFACT = "enter artifactId for searching";
    private static final String DESCRIPTION_FOR_GROUP_ID = "enter groupId for searching";
    private static final String DESCRIPTION_FOR_EXIT = "to shutdown";
    ExecutorService executor;
    Option artifactId = new Option("a", true, DESCRIPTION_FOR_ARTIFACT);
    Option groupId = new Option("g", true, DESCRIPTION_FOR_GROUP_ID);
    Option exit = new Option("exit", false, DESCRIPTION_FOR_EXIT);
    CommandLineParser parser = new DefaultParser();


    static final Logger logger = LogManager.getLogger(MavenGrabberService.class);

    public MavenGrabberService() {
        try {
            PropertiesConfiguration configuration = Configuration.getConfig();
            int poolSize = configuration.getInt("pool-size");
            this.executor = Executors.newFixedThreadPool(poolSize);
            artifactId.setArgs(Option.UNLIMITED_VALUES);
            artifactId.setValueSeparator(',');
            groupId.setArgs(Option.UNLIMITED_VALUES);
            options.addOption(artifactId);
            options.addOption(groupId);
            options.addOption(exit);
            logger.info("Maven Grabber Service has been created: ");
        } catch (ConfigurationException configurationException) {
            logger.error("no such configuration:" + configurationException.getMessage());
        }
    }

    public void parseArguments(String[] parameters) {
        try {

            String[] args = new String[]{Arrays.toString(parameters)};
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("a")) {
                executeRequest(cmd.getOptionValues("a"));
            } else if (cmd.hasOption("g")) {
                executeRequest(cmd.getOptionValues("g"));
            } else if (cmd.hasOption("exit")) {
                logger.info("Shutting down");
                System.exit(1);
            } else {
                System.out.println("No such options");
                logger.info("wrong arguments");

            }
        } catch (Exception exception) {
            formatter.printHelp("use one of the following commands", options);
            logger.error("error: " + exception.getMessage());
        }
    }

    public void executeRequest(String[] parameters) {
        Arrays.stream(parameters)
                .filter(parameter -> !parameter.equals(""))
                .forEach(parameter -> {
                    parameter = parameter.trim();
                    logger.info("parsing argument:" + parameter);
                    try {
                        new MavenGrabberExecutor(parameter, executor);
                    } catch (ConfigurationException e) {
                        logger.error("config exception: " + e.getMessage());
                    }
                });
    }
}
