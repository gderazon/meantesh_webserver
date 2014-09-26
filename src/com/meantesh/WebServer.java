package com.meantesh;
 
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.meantesh.handlers.HttpHandler;
import com.meantesh.handlers.StaticContentHandler;
import com.meantesh.responses.BadRequest400;
import com.meantesh.responses.InternalServerError500;
 
/**
 * Simple Java non-blocking NIO webserver.
 * @author Golan Derazon based on md_5
 */
public class WebServer implements Runnable {
 
	private final static Logger logger = Logger.getLogger(WebServer.class.getName());
	
    private Selector selector = Selector.open();
    private ServerSocketChannel server = ServerSocketChannel.open();
    private volatile boolean isRunning = true;
    private  StaticContentHandler staticContentHandler;
    private ServerConf serverConf;
    private int sessionsCount;

    /**
     * Create a new server and immediately binds it.
     *
     * @param address the address to bind on
     * @throws IOException if there are any errors creating the server.
     */
    protected WebServer() throws IOException{
    }
    
    protected void initServer() throws Exception {
    	serverConf= new ServerConf();
    	serverConf.loadConfiguration();
        server.socket().bind(new InetSocketAddress(serverConf.getAddress(),serverConf.getPort()));
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);
        staticContentHandler = new StaticContentHandler(serverConf);
    }
    
    public void runServer() throws Exception {
    	initServer();
        logger.info("Meantesh server started and waiting for requests.");
        while (isRunning) {
            run();
            Thread.sleep(10);
        }	
    }
    
    /**
     * Core run method. This is not a thread safe method, however it is non
     * blocking. If an exception is encountered it will be thrown wrapped in a
     * RuntimeException, and the server will automatically be {@link #shutDown}
     */
    @Override
    public final void run() {
        try {
            selector.selectNow();
            Iterator<SelectionKey> i = selector.selectedKeys().iterator();
            while (i.hasNext()) {
                SelectionKey key = i.next();
                i.remove();
                if (!key.isValid()) {
                    continue;
                }
                try {
                    // get a new connection
                    if (key.isAcceptable()) {
                        // accept them
                        SocketChannel client = server.accept();
                        // non blocking please
                        client.configureBlocking(false);
                        // show out intentions
                        client.register(selector, SelectionKey.OP_READ);
                        // read from the connection
                    } else if (key.isReadable()) {
                        //  get the client
                        SocketChannel client = (SocketChannel) key.channel();
                        // get the session
                        HTTPSession session = (HTTPSession) key.attachment();
                        // create it if it doesnt exist
                        if (session == null) {
                            session = new HTTPSession(client);
                            key.attach(session);
                        }
                        // get more data
                        boolean hasData = session.readData();
                        
                        if (hasData){
                            // decode the message
                            String line = session.readLine();
                            if (line == null){
                            	throw new BadRequest();
                            }
                            while (line != null && !line.isEmpty()) {
                            	line = session.readLine();
                            }
                        	HTTPRequest request = null;
                        	try {
                        		request = new HTTPRequest(session.getLines());
                        	}catch (Exception e){
                        		throw new BadRequest();
                        	}
                            session.sendResponse(handle(session, request));
                            ++sessionsCount;
                        }
                    }
                }catch (BadRequest br){
                    if (key.attachment() instanceof HTTPSession) {
                        ((HTTPSession) key.attachment()).sendResponse(new BadRequest400(""));
                    }
                } catch (Exception ex) {
                	logger.log(Level.SEVERE, "Error handling client: " + key.channel(),ex);
                }finally {
                    if (key.attachment() instanceof HTTPSession) {
                        ((HTTPSession) key.attachment()).close();
                    }                    	
                }
            }
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Error in server execution. Halting server.",t);
        	shutdown();
            System.exit(1);
        }
    }
 
    /**
     * Handle a web request.
     *
     * @param session the entire http session
     * @return the handled request
     */
    protected HTTPResponse handle(HTTPSession session, HTTPRequest request) {
    	try {
    		HttpHandler handler = serverConf.getHandlerForLocation(request.getUri());
    		if (handler != null) {
    			return handler.handleRequest(request);
    		}
    		return staticContentHandler.handleRequest(request);
    	}catch (Throwable t) {
    		return new InternalServerError500(t);
    	}
    }
 
    /**
     * Shutdown this server, preventing it from handling any more requests.
     */
    public final void shutdown() {
        isRunning = false;
        try {
            selector.close();
            server.close();
        } catch (IOException ex) {
            // do nothing, its game over
        	logger.finest("Error closing connection");
        }
    }
    
    public int getSessionsCount(){
    	return sessionsCount;
    }
    
    public static void main(String[] args) throws Exception {
        WebServer server = new WebServer();
        server.runServer();
    }
}