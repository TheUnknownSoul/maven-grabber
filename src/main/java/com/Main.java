package com;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.service.MavenGrabberService;

public class Main {

    static final Logger logger = LogManager.getRootLogger();

    public static void main(String[] args) throws Exception {

        MavenGrabberService service = new MavenGrabberService();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String userInput;
        logger.info("Launching...");
        do {
            System.out.println("enter artifact or group id");
            userInput = reader.readLine();
            service.parseArguments(new String[]{userInput});
        } while (!userInput.equals("-exit"));

    }

}
