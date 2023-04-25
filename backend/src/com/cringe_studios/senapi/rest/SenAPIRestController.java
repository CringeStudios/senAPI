package com.cringe_studios.senapi.rest;

import java.util.UUID;

import com.cringe_studios.senapi.SenAPI;
import com.cringe_studios.senapi.database.RequestStatus;
import com.cringe_studios.senapi.database.SenpaiRequest;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.JSONParseException;
import me.mrletsplay.mrcore.json.JSONType;
import me.mrletsplay.simplehttpserver.http.HttpRequestMethod;
import me.mrletsplay.simplehttpserver.http.HttpStatusCodes;
import me.mrletsplay.simplehttpserver.http.endpoint.Endpoint;
import me.mrletsplay.simplehttpserver.http.endpoint.RequestParameter;
import me.mrletsplay.simplehttpserver.http.endpoint.rest.PartialRestController;
import me.mrletsplay.simplehttpserver.http.header.DefaultClientContentTypes;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.simplehttpserver.http.response.JsonResponse;

public class SenAPIRestController extends PartialRestController {

	public SenAPIRestController(String basePath) {
		super(basePath);
	}

	private JSONObject error(String errorMessage) {
		JSONObject error = new JSONObject();
		error.put("error", errorMessage);
		return error;
	}

	@Override
	public void index() {
		HttpRequestContext.getCurrentContext().respond(HttpStatusCodes.ACCESS_DENIED_403, new JsonResponse(error("You may not spy on other people's senpai requests")));
	}

	@Override
	public void store() {
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();

		JSONObject data;
		try {
			data = ctx.getClientHeader().getPostData().getParsedAs(DefaultClientContentTypes.JSON_OBJECT);
		}catch(JSONParseException | ClassCastException e) {
			ctx.respond(HttpStatusCodes.BAD_REQUEST_400, new JsonResponse(error("Bad JSON")));
			return;
		}

		if(!data.isOfType("sender", JSONType.STRING)
			|| !data.isOfType("recipient", JSONType.STRING)
			|| !data.isOfType("message", JSONType.STRING)) {
			ctx.respond(HttpStatusCodes.BAD_REQUEST_400, new JsonResponse(error("sender, recipient and message must be set")));
			return;
		}

		SenpaiRequest request = new SenpaiRequest(
			UUID.randomUUID().toString(),
			data.getString("sender"),
			data.getString("recipient"),
			data.getString("message"));

		SenAPI.getDatabase().store(request);

		ctx.respond(HttpStatusCodes.OK_200, new JsonResponse(request.toRestJSON()));
	}

	@Override
	public void show(String id) {
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();

		SenpaiRequest request = SenAPI.getDatabase().get(id);
		if(request == null) {
			ctx.respond(HttpStatusCodes.NOT_FOUND_404, new JsonResponse(error("No senpai request with that id")));
			return;
		}

		ctx.respond(HttpStatusCodes.OK_200, new JsonResponse(request.toRestJSON()));
	}

	@Endpoint(method = HttpRequestMethod.PUT, path = "/{id}/accept", pathPattern = true)
	public void accept(@RequestParameter("id") String id) {
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();

		SenpaiRequest request = SenAPI.getDatabase().get(id);
		if(request == null) {
			ctx.respond(HttpStatusCodes.NOT_FOUND_404, new JsonResponse(error("No senpai request with that id")));
			return;
		}

		if(request.getStatus() != RequestStatus.PENDING) {
			ctx.respond(HttpStatusCodes.BAD_REQUEST_400, new JsonResponse(error("Senpai request not pending")));
			return;
		}

		request.setStatus(RequestStatus.ACCEPTED);
		SenAPI.getDatabase().save();
		ctx.respond(HttpStatusCodes.OK_200, new JsonResponse(request.toRestJSON()));
	}

	@Endpoint(method = HttpRequestMethod.PUT, path = "/{id}/reject", pathPattern = true)
	public void reject(@RequestParameter("id") String id) {
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();

		SenpaiRequest request = SenAPI.getDatabase().get(id);
		if(request == null) {
			ctx.respond(HttpStatusCodes.NOT_FOUND_404, new JsonResponse(error("No senpai request with that id")));
			return;
		}

		if(request.getStatus() != RequestStatus.PENDING) {
			ctx.respond(HttpStatusCodes.BAD_REQUEST_400, new JsonResponse(error("Senpai request not pending")));
			return;
		}

		request.setStatus(RequestStatus.REJECTED);
		SenAPI.getDatabase().save();
		ctx.respond(HttpStatusCodes.OK_200, new JsonResponse(request.toRestJSON()));
	}

}
