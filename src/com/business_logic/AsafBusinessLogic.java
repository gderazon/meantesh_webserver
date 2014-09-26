package com.business_logic;

import com.meantesh.HTTPRequest;
import com.meantesh.HTTPResponse;
import com.meantesh.handlers.HttpHandler;

public class AsafBusinessLogic implements HttpHandler {

	@Override
	public HTTPResponse handleRequest(HTTPRequest request) throws Exception {
		// TODO Auto-generated method stub
		HTTPResponse resp =  new HTTPResponse();
		resp.setContent("{'name':asaf}".getBytes());
		return resp;
	}

}
