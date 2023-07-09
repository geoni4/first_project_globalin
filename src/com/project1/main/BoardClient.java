package com.project1.main;

import java.net.Socket;

import com.project1.client.ClientTransfer;

public class BoardClient {
	public static void main(String[] args) {
		String serverIP = "127.0.0.1";
		final int PORT = 7878;
		try {
			Socket socket = new Socket(serverIP, PORT);
			
			System.out.println("서버에 연결 되었습니다.");
			ClientTransfer clientTransfer = new ClientTransfer(socket);
			
			clientTransfer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
