package com.meantesh.handlers;

import com.meantesh.HTTPRequest;
import com.meantesh.HTTPResponse;

/**
 * This interface should be implemented by business logic class, 
 * which want to handle requests sent to the meantesh server.
 * @see server.yml to see how to hook your business logic into the server.
 * @author gderazon
 *
 */
public interface HttpHandler {
	
	public HTTPResponse handleRequest(HTTPRequest request) throws Exception;
	
}
