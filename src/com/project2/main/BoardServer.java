package com.project2.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import com.project2.server.SocketClient;



public class BoardServer{
	public static void main(String[] args) {
		try {
			BoardServer boardServer = new BoardServer();
			boardServer.start();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void start() throws IOException{
		serverSocket = new ServerSocket(PORT);
		System.out.println("서버 부팅 됨.");
		
		Thread thread = new Thread(() ->{
			try {
				while(true) {
					Socket socket = serverSocket.accept();
					SocketClient socketClient = new SocketClient(this, socket);
				}
			}catch (Exception e) {
				
			}
		});
		thread.start();
	}


	private ServerSocket serverSocket;
	private ExecutorService threadPool = Executors.newFixedThreadPool(100);
	private final int PORT = 7878;
	private Map<String, SocketClient> boardConn = Collections.synchronizedMap(new HashMap<String, SocketClient>()) ;
	

	public void removeSocketClient(SocketClient socketClient) {
		String key = socketClient.getRandomNum() + "@" + socketClient.getClientIP();
		boardConn.remove(key);
		System.out.println("나감: " + key);
		System.out.println("현재 접속인원 수: " + boardConn.size() + '\n');
	}
	
	public void addSocketClient(SocketClient socketClient) {
		String key = socketClient.getRandomNum() + "@" + socketClient.getClientIP();
		boardConn.put(key, socketClient);
		System.out.println("입장: " + key);
		System.out.println("현재 접속인원 수: " + boardConn.size() + '\n');
	}
	
	public void send(SocketClient sender, String command) {
		JSONObject root = new JSONObject();
		root.put("randomNum",  sender.getRandomNum());
		root.put("clientIP",  sender.getClientIP());
		root.put("command",  command);
		
		String json = root.toString();

		sender.send(json);
	}
	

	public void stop() {
		try {
			serverSocket.close();
			threadPool.shutdownNow();
			boardConn.values().stream().forEach(sc -> sc.close());
			
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	public ExecutorService getThreadPool() {
		return threadPool;
	}
	
	public void setThreadPool(ExecutorService threadPool) {
		this.threadPool = threadPool;
	}
}

