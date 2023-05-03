package com.cringe_studios.senapi.database;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.JSONType;
import me.mrletsplay.mrcore.json.converter.JSONComplexListType;
import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONConvertible;
import me.mrletsplay.mrcore.json.converter.JSONListType;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.mrcore.json.converter.SerializationOption;

public class SenpaiRequest implements JSONConvertible {

	@JSONValue
	private String
		id,
		sender,
		message;

	@JSONValue
	@JSONComplexListType(Recipient.class)
	private List<Recipient> recipients;
	
	private Instant timestamp;

	@JSONConstructor
	private SenpaiRequest() {}

	public SenpaiRequest(String id, String sender, List<Recipient> recipients, String message) {
		this.id = id;
		this.sender = sender;
		this.recipients = recipients;
		this.message = message;
		this.timestamp = Instant.now();
	}

	public String getId() {
		return id;
	}

	public String getSender() {
		return sender;
	}

	public List<Recipient> getRecipients() {
		return recipients;
	}

	public String getMessage() {
		return message;
	}

	public void setStatus(String name, RequestStatus status) throws RecipientException {
		Recipient r = recipients.stream().filter(rec -> rec.getName().equals(name)).findFirst().orElse(null);
		if(r == null) throw new RecipientException("Recipient name does not exist");
		r.setStatus(status);
	}

	public RequestStatus getStatus(String name) throws RecipientException {
		Recipient r = recipients.stream().filter(rec -> rec.getName().equals(name)).findFirst().orElse(null);
		if(r == null) throw new RecipientException("Recipient name does not exist");
		return r.getStatus();
	}

	@Override
	public JSONObject toJSON() {
		return toJSON(SerializationOption.DONT_INCLUDE_CLASS, SerializationOption.SHORT_ENUMS);
	}

	public JSONObject toRestJSON() {
		JSONObject obj = toJSON();
		obj.put("timestamp", timestamp.toString());
		return obj;
	}

	@Override
	public void preSerialize(JSONObject object) {
		object.put("timestamp", timestamp.toEpochMilli());
	}

	@Override
	public void preDeserialize(JSONObject object) {
		this.timestamp = Instant.ofEpochMilli(object.getLong("timestamp"));
	}

}
