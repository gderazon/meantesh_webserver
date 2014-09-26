package com.meantesh.handlers;

import java.io.File;
import org.apache.commons.io.FileUtils;

import com.meantesh.HTTPRequest;
import com.meantesh.HTTPResponse;
import com.meantesh.ServerConf;
import com.meantesh.responses.NotFound404;

public class StaticContentHandler implements HttpHandler {
	
	private ServerConf serverConf;
	
	public StaticContentHandler(ServerConf configuration){
		this.serverConf = configuration;
	}
	
	public HTTPResponse handleRequest(HTTPRequest request) throws Exception{
        HTTPResponse response = new HTTPResponse();
        File f = null;
        if ("/".equals(request.getLocation()) || "".equals(request.getLocation())){
        	f = new File(serverConf.getRootDocument(),"server_default.html");
        } else{
        	f = new File(serverConf.getRootDocument(),request.getUri());
        }
        
        if (!f.exists()){
        	return new NotFound404(f.getName());
        }
        byte[] bytes = FileUtils.readFileToByteArray(f);
        response.setContent(bytes);
        return response;
		
	}
}
