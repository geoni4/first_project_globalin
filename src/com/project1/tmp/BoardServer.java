package com.project1.tmp;

import java.net.ServerSocket;
import java.net.Socket;

public class BoardServer {
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(7777);
			Socket socket = null;
			System.out.println("서버가 대기 중입니다.");
			socket = serverSocket.accept();
			System.out.println("클라이언트가 접속 됐습니다. " + socket.getInetAddress().getHostName() + "님 환영합니다.");
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
