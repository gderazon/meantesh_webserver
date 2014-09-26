package com.meantesh;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Once a channel is opened between the client and the server, the HTTP session takes care of reading the data, 
 * and sending the response. Once response is sent, connection is closed.
 * @author gderazon
 */
public class HTTPSession {
	private final static Logger logger = Logger.getLogger(HTTPSession.class.getName());
	
	private Charset charset = Charset.forName("UTF-8");
	private CharsetEncoder encoder = charset.newEncoder();
    private final SocketChannel channel;
    private final ByteBuffer buffer = ByteBuffer.allocate(2048);
    private final StringBuilder readLines = new StringBuilder();
    private int mark = 0;

    public HTTPSession(SocketChannel channel) {
        this.channel = channel;
    }
        
    /**
     * Try to read a line.
     */
    public String readLine() throws IOException {
        StringBuilder sb = new StringBuilder();
        int l = -1;
        while (buffer.hasRemaining()) {
            char c = (char) buffer.get();
            sb.append(c);
            if (c == '\n' && l == '\r') {
                // mark our position
                mark = buffer.position();
                // append to the total
                readLines.append(sb);
                // return with no line separators
                return sb.substring(0, sb.length() - 2);
            }
            l = c;
        }
        return null;
    }

    /**
     * Get data from the stream.
     */
    public boolean readData() throws IOException {
        buffer.limit(buffer.capacity());
        int read = channel.read(buffer);
        if (read == -1) {
            return false;
        }
        buffer.flip();
        buffer.position(mark);
        return true;
    }
    
    /**
     * 
     */
    public void sendResponse(HTTPResponse response) {
        response.addDefaultHeaders();
        try {
            writeLine(response.getVersion() + " " + response.getResponseCode() + " " + response.getResponseReason());
            for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
                writeLine(header.getKey() + ": " + header.getValue());
            }
            writeLine("");
            channel.write(ByteBuffer.wrap(response.getContent()));
        } catch (IOException ex) {
            logger.log(Level.SEVERE,"Error  sending response.",ex);
        }
    }
    
    public String getLines(){
    	return readLines.toString();
    }
    
    public void close() {
        try {
            channel.close();
        } catch (IOException ex) {
        	logger.log(Level.FINE,"Error closing connection.",ex);
        }
    }

    private void writeLine(String line) throws IOException {
        channel.write(encoder.encode(CharBuffer.wrap(line + "\r\n")));
    }
    
}
