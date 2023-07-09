package com.project1.tmp;

import java.io.DataInputStream;
import java.net.Socket;

public class Receiver extends Thread{
	private Socket socket=null;
	private DataInputStream in = null;

	public Receiver(Socket socket) {
		this.socket = socket;
		try {
			in = new DataInputStream(socket.getInputStream());
		} catch (Exception e) {
		}
	}

	@Override
	public void run() {
		try {
			while(true) {
				if(in == null) break;
				System.out.println(in.readUTF());
				
			}
		}catch (Exception e) {
			
		}
	}
	
}
