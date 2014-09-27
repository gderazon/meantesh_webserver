package com.meantesh.responses;

import com.meantesh.HTTPResponse;

public class HttpResponseBadRequest extends HTTPResponse {
	public HttpResponseBadRequest(String resource){
		super(400,"BAD Request");
		setContent("Bad Request sent by client");
	}
}
