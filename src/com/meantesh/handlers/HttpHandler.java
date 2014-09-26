package com.meantesh.handlers;

import com.meantesh.HTTPRequest;
import com.meantesh.HTTPResponse;

public interface HttpHandler {
	
	public HTTPResponse handleRequest(HTTPRequest request) throws Exception;
	
}
