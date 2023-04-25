package com.cringe_studios.senapi.database;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.converter.JSONConverter;

public class SenAPIDatabase {

	private Path databasePath;
	private List<SenpaiRequest> requests;

	public SenAPIDatabase(Path databasePath) {
		this.databasePath = databasePath;
		if(!Files.exists(databasePath)) {
			try {
				Files.createFile(databasePath);
			} catch (IOException e) {
				throw new RuntimeException("Failed to create DB", e);
			}
		}
		load();
	}

	private void load() {
		try {
			requests = new ArrayList<>();
			String dbString = Files.readString(databasePath, StandardCharsets.UTF_8);
			if(dbString.isEmpty()) {
				save();
				return;
			}

			JSONArray raw = new JSONArray(dbString);
			for(Object o : raw) {
				requests.add(JSONConverter.decodeObject((JSONObject) o, SenpaiRequest.class));
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to load DB", e);
		}
	}

	public void save() {
		JSONArray raw = new JSONArray();
		for(SenpaiRequest r : requests) {
			raw.add(r.toJSON());
		}

		try {
			Files.writeString(databasePath, raw.toFancyString(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load DB", e);
		}
	}

	public void store(SenpaiRequest request) {
		requests.add(request);
		save();
	}

	public SenpaiRequest get(String id) {
		return requests.stream()
			.filter(r -> r.getId().equals(id))
			.findFirst().orElse(null);
	}

}
