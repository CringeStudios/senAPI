package com.cringe_studios.senapi;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.cringe_studios.senapi.database.SenAPIDatabase;
import com.cringe_studios.senapi.rest.SenAPIRestController;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.JSONParseException;
import me.mrletsplay.simplehttpserver.http.server.HttpServer;
import me.mrletsplay.simplehttpserver.http.server.HttpServerConfiguration;

public class SenAPI {

	private static SenAPIDatabase database;

	public static void main(String[] args) throws IOException {
		Path configPath = Paths.get("config.json");
		if(!Files.exists(configPath)) Files.createFile(configPath);

		String configStr = Files.readString(configPath);
		if(configStr.isEmpty()) {
			JSONObject config = new JSONObject();
			config.put("host", "0.0.0.0");
			config.put("port", 42069);
			Files.writeString(configPath, config.toFancyString(), StandardCharsets.UTF_8);
			System.out.println("Config file created. Please edit it accordingly");
			return;
		}

		JSONObject config;
		try {
			config = new JSONObject(configStr);
		}catch(JSONParseException | ClassCastException e) {
			throw new RuntimeException("Failed to load config file", e);
		}

		database = new SenAPIDatabase(Paths.get("db.json"));

		HttpServer server = new HttpServer(new HttpServerConfiguration.Builder()
			.host(config.getString("host"))
			.port(config.getInt("port"))
			.create());

		new SenAPIRestController("/api/senpai").register(server.getDocumentProvider());

		server.start();
	}

	public static SenAPIDatabase getDatabase() {
		return database;
	}

}
