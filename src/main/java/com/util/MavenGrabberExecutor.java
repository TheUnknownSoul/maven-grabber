package com.util;

import com.configuration.Configuration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class MavenGrabberExecutor implements Callable<Object> {

    private static final Logger logger = LogManager.getLogger(MavenGrabberExecutor.class);
    private static final String REQUEST_ARGS = "select?q=a:%s&rows=%s";

    String param;
    String uri;
    String rows;

    public MavenGrabberExecutor(String arg, ExecutorService executor) throws ConfigurationException {
        PropertiesConfiguration configuration = Configuration.getConfig();
        this.uri = configuration.getString("uri");
        this.rows = configuration.getString("rows");
        this.param = arg;
        executor.submit(this);

    }

    @Override
    public Object call() {
        try {
            uri += String.format(REQUEST_ARGS, param, rows);
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String responseContentAsString = in.readLine();
            JsonElement json = gson.fromJson(responseContentAsString, JsonElement.class);
            System.out.println(gson.toJson(json));
            connection.disconnect();
            return String.valueOf(json);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return "bad request";
    }
}