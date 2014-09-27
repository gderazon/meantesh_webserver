package com.meantesh;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 
 * @author gderazon
 */
public class HTTPRequest {
	 
    private final String raw;
    private String method;
    private String location;
    private String uri;
    private String version;
    private Map<String, String> headers = new HashMap<String, String>();
    private Map<String, List<String>> parameters =new HashMap<String, List<String>>();
    private Map<String, List<String>> postParameters =new HashMap<String, List<String>>();
    
    
    public HTTPRequest(String raw) {
        this.raw = raw;
        parse();
    }
    
    public String getMethod() {
        return method;
    }

    public String getLocation() {
        return location;
    }

    public String getHead(String key) {
        return headers.get(key);
    }
    
    public String getVersion(){
    	return version;
    }
    
    public String getUri(){
    	return uri;
    }
    
    public Map<String, List<String>> getParameters(){
    	return parameters;
    }

    public Map<String, List<String>> postParameters(){
    	return postParameters;
    }

    /**
     * Parses data fetched from session to populate the HTTPRequest.
     */
    private void parse() {
        // parse the first line
        StringTokenizer tokenizer = new StringTokenizer(raw);
        method = tokenizer.nextToken().toUpperCase();
        location = tokenizer.nextToken();
        version = tokenizer.nextToken();
        // parse the headers
        String[] lines = raw.split("\r\n");
        for (int i = 1; i < lines.length; i++) {
        	if (lines[i].equals("")){
        		break;
        	}
            String[] keyVal = lines[i].split(":", 2);
            headers.put(keyVal[0], keyVal[1]);
        }
        String content = lines[lines.length-1];
        postParameters = decodeParameters(content);
        // Decode parameters from the location
        int qmi = location.indexOf('?');
        if (qmi >= 0) {
        	parameters = decodeParameters(location.substring(qmi + 1));
            uri = decodePercent(location.substring(0, qmi));
        } else {
            uri = decodePercent(location);
        }        
    }
    
    /**
     * Decode percent encoded <code>String</code> values.
     *
     * @param str the percent encoded <code>String</code>
     * @return expanded form of the input, for example "foo%20bar" becomes "foo bar"
     */
    private String decodePercent(String str) {
        String decoded = null;
        try {
            decoded = URLDecoder.decode(str, "UTF8");
        } catch (UnsupportedEncodingException ignored) {
        	return str;
        }
        return decoded;
    }    
    
    /**
     * 
     */
    private  Map<String, List<String>> decodeParameters(String queryString) {
        Map<String, List<String>> parms = new HashMap<String, List<String>>();
        if (queryString != null) {
            StringTokenizer st = new StringTokenizer(queryString, "&");
            while (st.hasMoreTokens()) {
                String e = st.nextToken();
                int sep = e.indexOf('=');
                String propertyName = (sep >= 0) ? decodePercent(e.substring(0, sep)).trim() : decodePercent(e).trim();
                if (!parms.containsKey(propertyName)) {
                    parms.put(propertyName, new ArrayList<String>());
                }
                String propertyValue = (sep >= 0) ? decodePercent(e.substring(sep + 1)) : null;
                if (propertyValue != null) {
                    parms.get(propertyName).add(propertyValue);
                }
            }
        }
        return parms;
    }    
}
