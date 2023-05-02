package com.cringe_studios.senapi.database;

import me.mrletsplay.mrcore.json.converter.JSONConvertible;
import me.mrletsplay.mrcore.json.converter.JSONValue;

public class Recipient implements JSONConvertible{
	
	@JSONValue
	private String name;
	
	@JSONValue
	private RequestStatus status;
	
	public Recipient(String name) {
		this.name = name;
		this.status = RequestStatus.PENDING;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RequestStatus getStatus() {
		return status;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}
}
