/**
 * WebServer.java    - Server  main event loop. 
 * HTTPSession.java  - Deals with the communication between server and client. 
 * HTTPRequest.java  - Parses and abstracts request data. Base class for all response types. 
 * HTTPResponse.java - Http Response data structure. Should be used as a base class for different return statuses responses.
 * ServerConf.java   - Deals with loading and accessing server configuration and URL mapping.
 * 
 * handlers\HttpHandler.java - Interface which should be implemented by business logic entities.
 * handlers\StaticContentHandler.java - Deals with static content.
 * 
 * responses\* - implemented http response for the different status codes
 */
package com.meantesh;
