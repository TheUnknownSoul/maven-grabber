package com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

public class MavenGrabberExecutor implements Callable<Object> {

	private static final Logger logger = LogManager.getLogger(MavenGrabberExecutor.class);

	String param;
	String uri;
	int poolSize;

	public MavenGrabberExecutor(String arg, String url, int threadPoolSize) {
		this.param = arg;
		this.uri = url;
		this.poolSize = threadPoolSize;
		ExecutorService executor = Executors.newFixedThreadPool(poolSize);
		executor.submit(this);

	}

	@Override
	public Object call() {
		try {

			uri += "select?q=a:" + param + "&rows=20";
			URL url = new URL(uri);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			int statusCode = connection.getResponseCode();
			if (statusCode == HttpURLConnection.HTTP_OK) {
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String responseContentAsString = in.readLine();
				JsonElement json = gson.fromJson(responseContentAsString, JsonElement.class);
				System.out.println(gson.toJson(json));
				connection.disconnect();

				return String.valueOf(json);
			}

		} catch (IOException e) {

			logger.error("exception  " + e.getMessage());
		}

		return "bad request";
	}
}