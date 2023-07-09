package com.project1.board.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import com.project1.board.DO.Board;
import com.project1.board.service.BoardService;

public class BoardController {
	private BoardService boardService = null;
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;

	public BoardController(Socket socket) {
		boardService = new BoardService();
		this.socket = socket;
		try {
			in = new DataInputStream(this.socket.getInputStream());
			out = new DataOutputStream(this.socket.getOutputStream());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void getList() {
		List<Board> boardList = boardService.findAll();
		try {
			StringBuilder tmp = new StringBuilder();
			tmp.append("글번호 \t | \t 제목 \t | \t 작성자 \t | \t 작성 날짜 \t\n");
			if(boardList != null) {
				for (Board board : boardList) {
					tmp.append(board.getBno()).append(" \t |")
						.append(" ").append(board.getTitle()).append(" \t |")
						.append(" ").append(board.getWriter()).append(" \t |")
						.append(" ").append(board.getCreateDate()).append("\n");
				}
			}
			if(boardList== null || boardList.size() == 0) {
				tmp = new StringBuilder().append("글이 없습니다.");
			}
			String summaryString = tmp.toString();
			send("0", summaryString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createContent() {
		Board board = new Board();
		try {
			send("1", "제목> ");
			board.setTitle(in.readUTF());
			send("1", "내용> ");
			board.setContent(in.readUTF());
			send("1", "작성자> ");
			
			board.setWriter(in.readUTF());
		} catch (Exception e) {
			return;
		}
		boardService.insertOne(board);
		try {
			send("0", "게시글 작성 완료.");
		} catch (Exception e) {
		}
	}

	public void getDetailContent() {
		int bno;
		try {
			send("1", "내용을 확인할 글 번호를 입력하세요> ");
			bno = Integer.valueOf(in.readUTF());
		} catch (Exception e) {
			return;
		}
		Board board;
		try {
			board = boardService.findOne(bno);
			String sendMessage = "글 없음.";
			if(board != null) {
				sendMessage = new StringBuilder()
						.append("글 번호 : ").append(board.getBno()).append("\n")
						.append("제목 : ").append(board.getTitle()).append("\n")
						.append("내용 : ").append(board.getContent()).append("\n")
						.append("작성자 : ").append(board.getWriter()).append("\n")
						.append("작성일자 : ").append(board.getCreateDate()).append("\n")
						.append("수정일자 : ").append(board.getModifyDate()).append("\n")
						.toString();
			}
			send("0", sendMessage);
		} catch (NullPointerException e) {
			System.out.println("자료가 없습니다.");
			return;
		} catch (IOException e) {

		}
	}

	public void deleteContent() {
		int bno = 0;
		try {
			send("1", "삭제할 글 번호를 입력하세요> ");
			bno = Integer.valueOf(in.readUTF());
		} catch (Exception e) {
			return;
		}
		try {
			if (boardService.deleteOne(bno)) {
				send("0", "글이 삭제되었습니다.");
			} else {
				send("0", "글이 없습니다.");
			}
		} catch (Exception e) {

		}
	}

	public void modifyContent() {
		int bno = 0;
		try {
			send("1", "수정할 글 번호를 입력하세요> ");
			bno = Integer.valueOf(in.readUTF());
		} catch (Exception e) {
			return;
		}
		Board board =null;
		try {
			board = boardService.findOne(bno);
			String sendMessage = "글 없음.";
			if(board != null) {
				sendMessage = new StringBuilder()
						.append("글 번호 : ").append(board.getBno()).append("\n")
						.append("제목 : ").append(board.getTitle()).append("\n")
						.append("내용 : ").append(board.getContent()).append("\n")
						.append("작성자 : ").append(board.getWriter()).append("\n")
						.append("작성일자 : ").append(board.getCreateDate()).append("\n")
						.append("수정일자 : ").append(board.getModifyDate()).append("\n")
						.append("수정하시겠습니까? (Y/N)").toString();
			}
			if(sendMessage.equals("글 없음.")) {
				send("0", sendMessage);
				return;
			}
			send("1", sendMessage);
			String menu = in.readUTF();
			if(!menu.equalsIgnoreCase("y")) {
				send("0", "수정 취소");
				return;
			}
			
			send("1", "제목> " + board.getTitle() + "\n바꿀 제목(입력 안할 시 수정 안 됨)> ");
			String title = in.readUTF();
			if( title.equals("")) {
				title = board.getTitle();
			}
			board.setTitle(title);
			send("1", "내용> "  + board.getContent() + "\n바꿀 내용(입력 안할 시 수정 안 됨)> ");
			String content = in.readUTF();
			if( content.equals("")) {
				content = board.getContent();
			}
			board.setContent(content);
			send("1", "작성자> " + board.getWriter() + "\n작성자명(입력 안할 시 수정 안 됨)> ");
			String writer = in.readUTF();
			if( writer.equals("")) {
				writer = board.getWriter();
			}
			board.setWriter(writer);
			board.setCreateDate(board.getCreateDate());
		} catch (NullPointerException e) {
			System.out.println("자료가 없습니다.");
			return;
		} catch (IOException e) {

		}
		boardService.updateOne(board);
		try {
			send("0", "게시굴 수정 완료.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			send("0", "종료합니다.");
			out.close();
			in.close();
		} catch (Exception e) {
			
		}
		
	}
	
	public void send(String occupy, String command) throws IOException  {
		out.writeUTF(occupy);
		out.writeUTF(command);
		out.flush();
	}
}
