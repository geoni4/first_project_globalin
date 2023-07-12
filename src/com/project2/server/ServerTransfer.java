package com.project2.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.project1.board.controller.BoardController;


public class ServerTransfer extends Thread{
	private Socket socket;
	private DataOutputStream out = null;
	private DataInputStream in = null;
	
	public ServerTransfer(Socket socket) {
		this.socket = socket;
		try {
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		String menu;
		BoardController boardController = new BoardController(socket);
		try {
			while (true) {
				if(out == null) break;
				if(in == null) break;
				menu = "1";
				try {
					System.out.println("메뉴 입력");
					menu = in.readUTF();
					System.out.println(menu);
				} catch (Exception e) {
					System.out.println("메뉴를 잘 입력하세요.");
				}
				if("1".equals(menu)) {
					System.out.println("리스트 출력");
					boardController.getPartialList();
				} else if("2".equals(menu)) {
					System.out.println("게시글 등록");
					boardController.createContent();
				} else if("3".equals(menu)) {
					System.out.println("게시물 내용");
					boardController.getDetailContent();
				} else if ("4".equals(menu)) {
					System.out.println("게시물 삭제");
					boardController.deleteContent();
				} else if ("5".equals(menu)) {
					System.out.println("게시물 수정");
					boardController.modifyContent();
				} else if("0".equals(menu)) {
					System.out.println("접속 종료.");
					boardController.close();
					close();
					break;
				} else {
					System.out.println("메뉴 입력 안 됨");
					boardController.defaultMethod();
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void close() throws IOException {
		out.close();
		in.close();
	}
}

