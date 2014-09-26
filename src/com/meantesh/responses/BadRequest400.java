package com.meantesh.responses;

import com.meantesh.HTTPResponse;

public class BadRequest400 extends HTTPResponse {
	public BadRequest400(String resource){
		super(400,"BAD Request");
		setContent("Bad Request sent by client");
	}
}
