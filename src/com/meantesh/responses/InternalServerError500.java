package com.meantesh.responses;

import com.meantesh.HTTPResponse;

public class InternalServerError500 extends HTTPResponse {
	public InternalServerError500(){
		super(500,"INTERNAL SERVER ERROR");
	}

	public InternalServerError500(Throwable t){
		this();
		setContent(String.format("Internal Server Error: %s ",t.getLocalizedMessage()));
	}
	
	public InternalServerError500(String message){
		this();
		setContent(String.format("Internal Server Error: %s ",message));
	}
	
}
