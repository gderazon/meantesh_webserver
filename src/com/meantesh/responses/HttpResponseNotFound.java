package com.meantesh.responses;

import com.meantesh.HTTPResponse;

public class HttpResponseNotFound extends HTTPResponse {
	public HttpResponseNotFound(String resource){
		super(404,"NOT FOUND");
		setContent(String.format("Resource %s not found!",resource).getBytes());
	}
}
