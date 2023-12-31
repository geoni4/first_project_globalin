package com.temp.main;

import java.net.ServerSocket;
import java.net.Socket;

import com.temp.server.ServerTransfer;

public class ProjectMain {
	public static void main(String[] args) {
		ServerSocket serverSocket =null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(7878);

			System.out.println("서버가 대기 중입니다.");
			
			socket = serverSocket.accept();
			
			ServerTransfer serverTransfer = new ServerTransfer(socket);
			
			serverTransfer.start();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}

