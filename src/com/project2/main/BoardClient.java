package com.project2.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import org.json.JSONObject;

import com.project1.client.ClientTransfer;

public class BoardClient {
	public static void main(String[] args) {
		try {
			BoardClient boardClient = new BoardClient();
			boardClient.setPosition("main");
			boardClient.connect();

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("position", position);
			jsonObject.put("command", "1");
			
			String json = jsonObject.toString();
			
			boardClient.send(json);
			
			boardClient.receive();
			
			Scanner scanner = new Scanner(System.in);
			
			while(true) {
				String command = scanner.nextLine();
				if(command.equals("0") && position.equals("main")) break;
				
				jsonObject = new JSONObject();
				jsonObject.put("position", position);
				jsonObject.put("command", command);
				json = jsonObject.toString();
				boardClient.send(json);
			}
			
			scanner.close();
			boardClient.unconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void receive() {
		Thread thread = new Thread(() -> {
			try {
				while (true) {
					String json = in.readUTF();
					JSONObject root = new JSONObject(json);
					String message = root.getString("message");
					System.out.println(message);
				}
			} catch (Exception e) {
				System.out.println("[클라이언트] 서버 연결 끊김");
				System.exit(0);
			}
		}) ;
		thread.start();
		
	}

	private Socket socket = null;
	private DataInputStream in = null;
	private DataOutputStream out = null;
	private final String serverIP = "127.0.0.1";
	private final int PORT = 7878;
	private static String position;

	private void connect() throws IOException {
		socket = new Socket(serverIP, PORT);
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());

		System.out.println("서버에 연결 되었습니다.");
	}
	
	public synchronized void setPosition(String position) {
		BoardClient.position = position;
	}
	
	public synchronized String getPosition() {
		return position;
	}
	
	public void send(String json) throws IOException {
		out.writeUTF(json);
		out.flush();
	}//send()
	
	public void unconnect() throws IOException {
		socket.close();
	}//unconnect()
	
	

}
