package com.project2.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import org.json.JSONObject;


public class BoardClient {
	public static void main(String[] args) {
		try {
			BoardClient boardClient = new BoardClient();
			position = "main";
			String command = "";
			boardClient.connect();

			JSONObject jsonObject = new JSONObject()
					.put("position", position)
					.put("data", "")
					.put("command", command);

			boardClient.send(jsonObject.toString());
			boardClient.receive();

			Scanner scanner = new Scanner(System.in);

			while (true) {
				command = scanner.nextLine();
				if ("0".equals(command) && "main".equals(position))
					break;

				jsonObject = new JSONObject()
						.put("position", position)
						.put("command", command);

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
					JSONObject receive = new JSONObject(in.readUTF());
					position = receive.getString("position");
					String data = receive.getString("data");
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
	}

}
