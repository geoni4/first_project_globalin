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
			position = "main";
			boardClient.connect();
			String command = "";

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("position", position);
			jsonObject.put("data", "");
			jsonObject.put("command", "");

			String json = jsonObject.toString();

			boardClient.send(json);
			boardClient.receive();

			Scanner scanner = new Scanner(System.in);

			while (true) {
				command = scanner.nextLine();
				if (command.equals("0") && position.equals("main"))
					break;

				jsonObject = new JSONObject();
				jsonObject.put("position", position);
				jsonObject.put("command", command);

				boardClient.send(jsonObject.toString());
			}

			scanner.close();
			boardClient.unconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void receive() {
		Thread thread = new Thread(() -> {
			// TODO Auto-generated method stub
			try {
				while (true) {
					String json = in.readUTF();
					JSONObject root = new JSONObject(json);
					position = root.getString("position");
					String data = root.getString("data");
					System.out.println(data);
				}
			} catch (Exception e) {
				System.out.println("연결을 종료합니다.");
				System.exit(0);
			}
		});

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


	public void send(String json) throws IOException {
		out.writeUTF(json);
		out.flush();
	}// send()

	public void unconnect() throws IOException {
		socket.close();
	}// unconnect()

}
