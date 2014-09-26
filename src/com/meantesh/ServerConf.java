package com.meantesh;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.meantesh.handlers.HttpHandler;

public class ServerConf {
	private Map serverSettings;	
	private Map<Pattern,HttpHandler> urlMappingCompiled = new HashMap<Pattern,HttpHandler>();
	
	public void loadConfiguration() throws Exception {
    	YamlReader reader = new YamlReader(new FileReader("server.yml"));
    	Object object = reader.read();
    	Map map = (Map)object;
    	serverSettings = (Map)map.get("basic_conf");
    	ArrayList<Map<String,String>> obj =  (ArrayList<Map<String,String>>)map.get("url_mappings");
    	for (Map<String,String> mapping:obj){
			Pattern p = Pattern.compile(mapping.get("url_regexp"));
			Class handler = Class.forName(mapping.get("handler"));
			if (!HttpHandler.class.isAssignableFrom(handler)){
				throw new Exception("Illegal HTTP handler " + handler.getCanonicalName());
			}
			urlMappingCompiled.put(p,(HttpHandler)handler.newInstance());
		}
	}
	
	public String getAddress() {
		return (String)serverSettings.get("address");
	}
	
	public int getPort() {
		return Integer.parseInt((String)serverSettings.get("port"));
	}
	
	public String getRootDocument() {
		return (String)serverSettings.get("root_doc");
	}
	
	public HttpHandler getHandlerForLocation(String location){
		for (Pattern p:urlMappingCompiled.keySet()){
			Matcher matcher = p.matcher(location);
			if (matcher.find()){
				return urlMappingCompiled.get(p);
			}
		}
		return null;
	}
}
