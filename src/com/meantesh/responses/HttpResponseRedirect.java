package com.meantesh.responses;

import com.meantesh.HTTPResponse;

public class HttpResponseRedirect extends HTTPResponse {
	public HttpResponseRedirect(){
		super(302,"Redirect");
	}
	
	public HttpResponseRedirect(String redirectTo){
		this();
		this.setHeader("Location",redirectTo);
	}
	
}
