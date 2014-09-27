package com.meantesh.responses;

import com.meantesh.HTTPResponse;

public class HttpResponseInternalServerError extends HTTPResponse {
	public HttpResponseInternalServerError(){
		super(500,"INTERNAL SERVER ERROR");
	}

	public HttpResponseInternalServerError(Throwable t){
		this();
		setContent(String.format("Internal Server Error: %s ",t.getLocalizedMessage()));
	}
	
	public HttpResponseInternalServerError(String message){
		this();
		setContent(String.format("Internal Server Error: %s ",message));
	}
	
}
