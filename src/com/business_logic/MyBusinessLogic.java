package com.business_logic;

import java.util.List;

import com.meantesh.BadRequestException;
import com.meantesh.HTTPRequest;
import com.meantesh.HTTPResponse;
import com.meantesh.handlers.HttpHandler;
import com.meantesh.responses.HttpResponseBadRequest;
import com.meantesh.responses.HttpResponseRedirect;

public class MyBusinessLogic implements HttpHandler {

	@Override
	public HTTPResponse handleRequest(HTTPRequest request) throws Exception {
		if (request.getUri().endsWith("/get")){
			String getParams = request.getParameters().toString();
			return new HTTPResponse(getParams);
		}
		if (request.getUri().endsWith("/post")){
			String postParams = request.postParameters().toString();
			return new HTTPResponse(postParams);
		}
		if (request.getUri().endsWith("/error")){
				throw new Exception("Demo error");
		}
		if (request.getUri().endsWith("/badrequest")){
			return new HttpResponseBadRequest("Bad request");
		}
		if (request.getUri().endsWith("/redirect")){
			List<String> operation = request.getParameters().get("destination");
			String redirectUrl = operation.get(0);
			return new HttpResponseRedirect(redirectUrl);
		}
		if (request.getUri().endsWith("/form-page")){
			if (request.getMethod().equals("GET")){
				String form = "<html><form method='post'>First name: <input type=\"text\" name=\"firstname\"><br>Last name: <input type=\"text\" name=\"lastname\"><input type=\"submit\" value=\"Submit\"></form></html>";
				HTTPResponse resp =  new HTTPResponse(form);
				return resp;
			}else {
				HTTPResponse resp =  new HTTPResponse(request.postParameters().toString());
				return resp;				
			}
		}
		HTTPResponse resp =  new HTTPResponse("Golan Business Logic");
		return resp;
	}

}
