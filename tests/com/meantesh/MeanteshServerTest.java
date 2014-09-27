package com.meantesh;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MeanteshServerTest {
	private static WebServer server;
	private static Thread serverThread;
	private volatile boolean errorHappened = false;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		serverThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					server = new WebServer();
					server.runServer();
				} catch (Exception e) {

				}
			}
		});
		serverThread.start();
		Thread.sleep(3000);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		server.shutdown();
		serverThread.join();
	}

	@Test
	public void testGreenPath() throws Exception {		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://127.0.0.1:5555/");
		CloseableHttpResponse response1 = httpclient.execute(httpGet);
		assertTrue(EntityUtils.toString(response1.getEntity()).indexOf(
				"Meantesh") > -1);
		httpclient.close();
	}

	@Test
	public void testGreenPathWithPost() throws Exception {		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost("http://127.0.0.1:5555/api/golan/post");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("registrationid","123456789"));
		nameValuePairs.add(new BasicNameValuePair("registrationid","123456789"));
		nameValuePairs.add(new BasicNameValuePair("bame","zizi"));
		nameValuePairs.add(new BasicNameValuePair("banaeme","top"));
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));		
		CloseableHttpResponse response1 = httpclient.execute(httppost);
		String res = EntityUtils.toString(response1.getEntity());
		assertTrue(res.indexOf("registrationid") > -1);
		assertTrue(res.indexOf("bame") > -1);
		assertTrue(res.indexOf("registrationid") > -1);
		assertTrue(res. indexOf("banaeme") > -1);
		assertTrue(res.split("123456789", -1).length == 3);
		httpclient.close();
	}

	@Test
	public void testCorrupted() throws Exception {
		int sessionsBefore = server.getSessionsCount();
		Socket clientSocket = new Socket("127.0.0.1", 5555);
		DataOutputStream outToServer = new DataOutputStream(
				clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		outToServer.writeBytes("Kishkusbalabush\r\n");
		StringBuffer buffer = new StringBuffer();
		String modifiedSentence = inFromServer.readLine();
		while (modifiedSentence != null) {
			buffer.append(modifiedSentence);
			modifiedSentence = inFromServer.readLine();
		}
		clientSocket.close();
		assertTrue(buffer.indexOf("Bad Request sent by client") > -1);
		int sessionsAfter = server.getSessionsCount();
		assertTrue(sessionsAfter-sessionsBefore ==0);
	}
	
	@Test
	public void loadTest() throws Exception {
		int sessionsBefore = server.getSessionsCount();
		ArrayList<Thread> threadList = new ArrayList<>();
		for (int i= 0;i<10;i++){
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						for (int z=0;z<30;z++){
							testGreenPath();
						}
					}catch (Exception e){
						errorHappened = true;
					}
				}
			});
			
			t.start();
			threadList.add(t);
		}
		for (Thread t:threadList){
			t.join();
		}
		assertFalse(errorHappened);
		int sessionsAfter = server.getSessionsCount();
		assertTrue(sessionsAfter-sessionsBefore ==300);
	}
}
