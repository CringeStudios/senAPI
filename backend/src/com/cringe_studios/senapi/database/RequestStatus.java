package com.cringe_studios.senapi.database;

import me.mrletsplay.mrcore.json.converter.JSONPrimitiveStringConvertible;

public enum RequestStatus implements JSONPrimitiveStringConvertible {

	PENDING,
	ACCEPTED,
	REJECTED;

	@Override
	public String toJSONPrimitive() {
		return name();
	}

	public static RequestStatus decodePrimitive(Object primitive) {
		return valueOf((String) primitive);
	}

}
