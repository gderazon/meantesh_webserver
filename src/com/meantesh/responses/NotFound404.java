package com.meantesh.responses;

import com.meantesh.HTTPResponse;

public class NotFound404 extends HTTPResponse {
	public NotFound404(String resource){
		super(404,"NOT FOUND");
		setContent(String.format("Resource %s not found!",resource).getBytes());
	}
}
