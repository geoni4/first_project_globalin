package com.project2.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;



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
	}


	private ServerSocket serverSocket;
	private ExecutorService threadPool = Executors.newFixedThreadPool(100);
	private final int PORT = 7878;
	private Map<String, SocketClient> boardConn = Collections.synchronizedMap(new HashMap<String, SocketClient>()) ;

	

//	public void run() {
//		String menu;
//		BoardController boardController = new BoardController(socket);
//		try {
//			while (true) {
//				if(out == null) break;
//				if(in == null) break;
//				menu = "1";
//				try {
//					System.out.println("메뉴 입력");
//					menu = in.readUTF();
//					System.out.println(menu);
//				} catch (Exception e) {
//					System.out.println("메뉴를 잘 입력하세요.");
//				}
//				if("1".equals(menu)) {
//					System.out.println("리스트 출력");
//					boardController.getPartialList();
//				} else if("2".equals(menu)) {
//					System.out.println("게시글 등록");
//					boardController.createContent();
//				} else if("3".equals(menu)) {
//					System.out.println("게시물 내용");
//					boardController.getDetailContent();
//				} else if ("4".equals(menu)) {
//					System.out.println("게시물 삭제");
//					boardController.deleteContent();
//				} else if ("5".equals(menu)) {
//					System.out.println("게시물 수정");
//					boardController.modifyContent();
//				} else if("0".equals(menu)) {
//					System.out.println("접속 종료.");
//					boardController.close();
//					close();
//					break;
//				} else {
//					System.out.println("메뉴 입력 안 됨");
//					boardController.defaultMethod();
//				}
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//	}
	

	public void removeSocketClient(SocketClient socketClient) {
		String key = socketClient.randomNum + "@" + socketClient.clientIP;
		boardConn.remove(key);
		System.out.println("나감: " + key);
		System.out.println("현재 접속인원 수: " + boardConn.size() + '\n');
	}
	
	public void addSocketClient(SocketClient socketClient) {
		String key = socketClient.randomNum + "@" + socketClient.clientIP;
		boardConn.put(key, socketClient);
		System.out.println("입장: " + key);
		System.out.println("현재 채팅자 수: " + boardConn.size() + '\n');
	}
	
	public void send(SocketClient sender, String command) {
		JSONObject root = new JSONObject();
		root.put("randomNum",  sender.randomNum);
		root.put("clientIP",  sender.clientIP);
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

