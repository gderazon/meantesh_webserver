package com.business_logic;

import java.util.List;

import com.meantesh.HTTPRequest;
import com.meantesh.HTTPResponse;
import com.meantesh.handlers.HttpHandler;
import com.meantesh.responses.HttpResponseRedirect;

public class MyBusinessLogic implements HttpHandler {

	@Override
	public HTTPResponse handleRequest(HTTPRequest request) throws Exception {
		if (request.getUri().endsWith("/post")){
			String postParams = request.postParameters().toString();
			return new HTTPResponse(postParams);
		}
		if (request.getUri().endsWith("/error")){
				throw new Exception("Demo error");
		}
		if (request.getUri().endsWith("/redirect")){
			List<String> operation = request.getParameters().get("destination");
			String redirectUrl = operation.get(0);
			return new HttpResponseRedirect(redirectUrl);
		}
		
		HTTPResponse resp =  new HTTPResponse("Golan Business Logic");
		return resp;
	}

}
