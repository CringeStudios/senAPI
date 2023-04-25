package com.cringe_studios.senapi.database;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONConvertible;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.mrcore.json.converter.SerializationOption;

public class SenpaiRequest implements JSONConvertible {

	@JSONValue
	private String
		id,
		sender,
		recipient,
		message;

	@JSONValue
	private RequestStatus status;

	@JSONConstructor
	private SenpaiRequest() {}

	public SenpaiRequest(String id, String sender, String recipient, String message) {
		this.id = id;
		this.sender = sender;
		this.recipient = recipient;
		this.message = message;
		this.status = RequestStatus.PENDING;
	}

	public String getId() {
		return id;
	}

	public String getSender() {
		return sender;
	}

	public String getRecipient() {
		return recipient;
	}

	public String getMessage() {
		return message;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}

	public RequestStatus getStatus() {
		return status;
	}

	@Override
	public JSONObject toJSON() {
		return toJSON(SerializationOption.DONT_INCLUDE_CLASS, SerializationOption.SHORT_ENUMS);
	}

}
