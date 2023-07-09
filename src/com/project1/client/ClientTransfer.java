package com.project1.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientTransfer extends Thread{
	private Socket socket = null;
	private DataInputStream in = null;
	private DataOutputStream out = null;
	
	public ClientTransfer(Socket socket) {
		this.socket = socket;
		try {
			in = new DataInputStream(this.socket.getInputStream());
			out= new DataOutputStream(this.socket.getOutputStream());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void run() {
		Scanner scanner = new Scanner(System.in);
		String occupy = "0";
		try {
			while(true) {
				if(out == null )break;
				if(in == null )break;
				if(occupy.equals("0")) {
					System.out.println("메뉴를 입력하세요.");
					System.out.print("1. 목록  2. 등록  3. 내용  4. 삭제  5. 수정  0. 종료 > ");
				}
				String menu = scanner.nextLine();
				
				out.writeUTF(menu);
				out.flush();
				occupy = in.readUTF();
				String str = in.readUTF();
				System.out.println(str);
				switch(occupy) {
				case "0":
					switch(menu) {
					case "":
						menu = "1";
						break;
					case "0":
						out.close();
						in.close();
						out = null;
						in = null;
						break;
					}
				}
			}
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("접속 종료");
	}
	
	
}
