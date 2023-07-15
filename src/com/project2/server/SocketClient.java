package com.project2.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.json.JSONObject;

import com.project2.board.controller.BoardController;
import com.project2.main.BoardServer;


public class SocketClient {
	BoardServer boardServer;
	Socket socket;
	DataInputStream in;
	DataOutputStream out;
	String clientIP;
	String randomNum;
	
	
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getClientIP() {
		return clientIP;
	}

	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}

	public String getRandomNum() {
		return randomNum;
	}

	public void setRandomNum(String randomNum) {
		this.randomNum = randomNum;
	}

	public SocketClient(BoardServer boardServer, Socket socket) {
		try {
			this.boardServer = boardServer;
			this.socket = socket;
			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			this.clientIP = ((InetSocketAddress)socket.getRemoteSocketAddress()).getHostName();
			this.randomNum = String.valueOf((int)(Math.random()*1000000));
			receive();
		} catch(Exception e) {
		}
	}
	
	public void receive() {
		boardServer.getThreadPool().execute(()->{
			BoardController boardController = new BoardController(socket);
			try {
				boardServer.addSocketClient(this);
				while(true) {
					//System.out.println(receiveJson.toString());
					String command = new JSONObject(in.readUTF()).getString("command");
					
					if("1".equals(command)) {
						System.out.println("리스트 출력");
						boardController.getPartialList();
					} else if("2".equals(command)) {
						System.out.println("게시글 등록");
						boardController.createContent();
					} else if("3".equals(command)) {
						System.out.println("게시물 내용");
						boardController.getDetailContent();
					} else if ("4".equals(command)) {
						System.out.println("게시물 삭제");
						boardController.deleteContent();
					} else if ("5".equals(command)) {
						System.out.println("게시물 수정");
						boardController.modifyContent();
					} else if("0".equals(command)) {
						boardController.close();
						break;
					} else {
						System.out.println("메뉴 입력 안 됨");
						boardController.defaultMenu();
					}
				}
			}catch (Exception e) {
				System.out.println();
				System.out.println("접속 종료.");
				boardServer.removeSocketClient(this);
				close();
			}
		});
	}


	public void send(String json) {
		try {
			out.writeUTF(json);
			out.flush();
		} catch(Exception e) {
			
		}
	}//send()
	
	
	public void close() {
		try {
			socket.close();
		} catch (Exception e) {
			
		}
	}
	
}
