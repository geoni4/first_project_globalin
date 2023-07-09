package com.project1.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.project1.board.controller.BoardController;


public class ServerTransfer extends Thread{
	private Socket socket;
	private DataOutputStream out = null;
	private DataInputStream in = null;
	//private PrintWriter out = null;
	
	public ServerTransfer(Socket socket) {
		this.socket = socket;
		try {
			//out = new PrintWriter(socket.getOutputStream());
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		int menu = 0;
		BoardController boardController = new BoardController(socket);
		try {
			while (true) {
				if(out == null) break;
				if(in == null) break;
				try {
					menu =0;
					System.out.println("메뉴 입력");
					menu = Integer.valueOf(in.readUTF());
					System.out.println(menu);
				} catch (Exception e) {
					System.out.println("메뉴를 잘 입력하세요.");
				}
				if (menu == 1) {
					System.out.println("리스트 출력");
					boardController.getList();
				} else if (menu == 2) {
					System.out.println("게시글 등록");
					boardController.createContent();
				} else if (menu == 3) {
					System.out.println("게시물 내용");
					boardController.getDetailContent();
				} else if (menu == 4) {
					System.out.println("게시물 삭제");
					boardController.deleteContent();
				} else if(menu == 5) {
					System.out.println("게시물 수정");
					boardController.modifyContent();
				} else if (menu == 0) {
					System.out.println("접속 종료.");
					boardController.close();
					break;
				}
			}

		}catch (Exception e) {
			e.printStackTrace();
		}
			
	}
}
