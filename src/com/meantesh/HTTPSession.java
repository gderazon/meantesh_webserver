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
 * Once a channel is opened between the client and the server, the HTTP session
 * takes care of reading the data, and sending the response. Once response is
 * sent, connection is closed.
 * 
 * @author gderazon
 */
public class HTTPSession {
	private final static Logger logger = Logger.getLogger(HTTPSession.class
			.getName());
	private static int BUFFER_SIZE = 10240;
	private Charset charset = Charset.forName("UTF-8");
	private CharsetEncoder encoder = charset.newEncoder();
	private final SocketChannel channel;
	private final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
	private String requestData = null;

	public HTTPSession(SocketChannel channel) {
		this.channel = channel;
	}

	/**
	 * Get data from the stream.
	 */
	public boolean readData() throws IOException {
		buffer.clear();
		buffer.limit(buffer.capacity());
		int read = channel.read(buffer);
		if (read < 0) {
			return false;
		}
		byte[] res = new byte[read];
		buffer.flip();
		buffer.get(res);
		requestData = new String(res);
		return true;
	}

	/**
     * 
     */
	public void sendResponse(HTTPResponse response) {
		response.addDefaultHeaders();
		try {
			writeLine(response.getVersion() + " " + response.getResponseCode()
					+ " " + response.getResponseReason());
			for (Map.Entry<String, String> header : response.getHeaders()
					.entrySet()) {
				writeLine(header.getKey() + ": " + header.getValue());
			}
			writeLine("");
			channel.write(ByteBuffer.wrap(response.getContent()));
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Error  sending response.", ex);
		}
	}

	public String getRequestData() {
		return requestData;
	}

	public void close() {
		try {
			channel.close();
		} catch (IOException ex) {
			logger.log(Level.FINE, "Error closing connection.", ex);
		}
	}

	private void writeLine(String line) throws IOException {
		channel.write(encoder.encode(CharBuffer.wrap(line + "\r\n")));
	}

}
