package com.project1.tmp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Sender extends Thread{
	private Socket socket;
	private DataOutputStream out = null;
	
	public Sender(Socket socket) {
		this.socket = socket;
		try {
			out = new DataOutputStream(socket.getOutputStream());
		} catch (Exception e) {
			
		}

	}

	@Override
	public void run() {
		Scanner scanner = new Scanner(System.in);
		try {
			while(true) {
				if(out == null) break;
				out.writeUTF(scanner.nextLine());
			}
		}catch (Exception e) {
		
		}
			
	}
}
