package com.meantesh;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Http Response data structure. 
 * Should be used as a base class for different return statuses responses.
 * @author gderazon
 */
public class HTTPResponse {
	 
    private String version = "HTTP/1.1";
    private int responseCode = 200;
    private String responseReason = "OK";
    private Map<String, String> headers = new LinkedHashMap<String, String>();
    private byte[] content =new byte[0];
    
    public HTTPResponse() {
    	
    }

    public HTTPResponse(String content) {
    	this();
    	setContent(content);
    }

    public HTTPResponse(int responseCode,String responseReason) {
    	this.responseCode = responseCode;
    	this.responseReason = responseReason;
    }
    
    public void addDefaultHeaders() {
        headers.put("Date", new Date().toString());
        headers.put("Server", "Java NIO Webserver by Golan D inspired by md_5");
        headers.put("Connection", "close");
        headers.put("Content-Length", Integer.toString(content.length));
    }
    
    public String getVersion(){
    	return version;
    }
    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseReason() {
        return responseReason;
    }

    public String getHeader(String header) {
        return headers.get(header);
    }

    public byte[] getContent() {
        return content;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public void setResponseReason(String responseReason) {
        this.responseReason = responseReason;
    }

    public void setContent(String content) {
        setContent(content.getBytes());
        
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }
    
    public Map<String,String> getHeaders(){
    	return headers;
    }
    
}