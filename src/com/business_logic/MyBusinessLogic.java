package com.business_logic;

import java.util.List;

import com.meantesh.HTTPRequest;
import com.meantesh.HTTPResponse;
import com.meantesh.handlers.HttpHandler;
import com.meantesh.responses.HttpResponseRedirect;

public class MyBusinessLogic implements HttpHandler {

	@Override
	public HTTPResponse handleRequest(HTTPRequest request) throws Exception {
		if (request.getParameters().containsKey("operation")){
			List<String> operation = request.getParameters().get("operation");
			if (operation.get(0).equals("error")){
				throw new Exception("Demo error");
			}
		}

		if (request.getParameters().containsKey("redirect")){
			List<String> operation = request.getParameters().get("redirect");
			String redirectUrl = operation.get(0);
			return new HttpResponseRedirect(redirectUrl);
		}
		
		// TODO Auto-generated method stub
		HTTPResponse resp =  new HTTPResponse();
		resp.setContent("{'name':golan}");
		return resp;
	}

}
