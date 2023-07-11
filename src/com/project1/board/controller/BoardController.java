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

	public void getPartialList() {
		int page = 1;
		int tmpPage = 1;
		String occupy = "1";
		try {
			while (true) {
				int allPage = (boardService.findAll().size()-1)/5+1;
				List<Board> boardList = boardService.findPartailinPage(page);
				StringBuilder tmp = new StringBuilder();
				String bnoStr = "글번호     ";
				String titleStr = "  제목                ";
				String writerStr = "  작성자        ";
				String createdDateStr = " 작성 날짜                          ";
				tmp.append(bnoStr).append("|").append(titleStr).append("|").append(writerStr).append("|")
						.append(createdDateStr).append(" |\n");
				if (boardList == null || boardList.size() == 0) {
					tmp = new StringBuilder().append("글이 없습니다.");
					occupy = "0";
				}
				if (boardList != null) {
					for (Board board : boardList) {
						tmp.append(addSpace(bnoStr, board.getBno())).append("|")
								.append(addSpace(titleStr, board.getTitle())).append("|")
								.append(addSpace(writerStr, board.getWriter())).append("|")
								.append(addSpace(createdDateStr, board.getCreatedDate())).append("|\n");
					}
					tmp.append("현재 페이지 : ").append(page).append(", ").append(page).append(" / ")
					.append(allPage).append("\n")
					.append("명령어를 입력하세요. (<: 왼쪽 페이지, >: 오른쪽 페이지, 숫자: 해당 페이지, ")
					.append("0 or 1페이지에서 <: 목록 출력 종료)");
				}
				String summaryString = tmp.toString();
				if(occupy.equals("0")) {
					send(occupy, "");
					break;
				}
				send(occupy, summaryString);
				String cmd = in.readUTF();
				if (">".equals(cmd))	tmpPage++;
				if ("<".equals(cmd))	tmpPage--;
				if (isInteger(cmd)) {
					tmpPage = Integer.valueOf(cmd);
				}
				if (tmpPage > allPage || tmpPage < 0)	{
					tmpPage = page;
					continue;
				}
				page = tmpPage;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getList() {
		List<Board> boardList = boardService.findAll();
		try {
			StringBuilder tmp = new StringBuilder();
			String bnoStr = "글번호     ";
			String titleStr = "  제목                ";
			String writerStr = "  작성자        ";
			String createdDateStr = " 작성 날짜                          ";
			tmp.append(bnoStr).append("|").append(titleStr).append("|").append(writerStr).append("|")
					.append(createdDateStr).append("|\n");
			if (boardList != null) {
				for (Board board : boardList) {
					tmp.append(addSpace(bnoStr, board.getBno())).append("|")
							.append(addSpace(titleStr, board.getTitle())).append("|")
							.append(addSpace(writerStr, board.getWriter())).append("|")
							.append(addSpace(createdDateStr, board.getCreatedDate())).append("|\n");
				}
			}
			if (boardList == null || boardList.size() == 0) {
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
			if (board != null) {
				sendMessage = new StringBuilder()
						.append("글 번호 : ").append(board.getBno()).append("\n")
						.append("제목 : ").append(board.getTitle()).append("\n")
						.append("내용 : ").append(board.getContent()).append("\n")
						.append("작성자 : ").append(board.getWriter()).append("\n")
						.append("작성일자 : ")	.append(board.getCreatedDate()).append("\n")
						.append("수정일자 : ").append(board.getModifiedDate())	.append("\n")
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
		Board board = null;
		try {
			board = boardService.findOne(bno);
			String sendMessage = "글 없음.";
			if (board != null) {
				sendMessage = new StringBuilder()
						.append("글 번호 : ").append(board.getBno()).append("\n")
						.append("제목 : ").append(board.getTitle()).append("\n")
						.append("내용 : ").append(board.getContent()).append("\n")
						.append("작성자 : ").append(board.getWriter()).append("\n")
						.append("작성일자 : ")	.append(board.getCreatedDate()).append("\n")
						.append("수정일자 : ").append(board.getModifiedDate())	.append("\n")
						.append("수정하시겠습니까? (Y/N)").toString();
			}
			if (sendMessage.equals("글 없음.")) {
				send("0", sendMessage);
				return;
			}
			send("1", sendMessage);
			String menu = in.readUTF();
			if (!menu.equalsIgnoreCase("y")) {
				send("0", "수정 취소");
				return;
			}

			send("1", "제목> " + board.getTitle() + "\n바꿀 제목(입력 안할 시 수정 안 됨)> ");
			String title = in.readUTF();
			if (title.equals("")) {
				title = board.getTitle();
			}
			board.setTitle(title);
			send("1", "내용> " + board.getContent() + "\n바꿀 내용(입력 안할 시 수정 안 됨)> ");
			String content = in.readUTF();
			if (content.equals("")) {
				content = board.getContent();
			}
			board.setContent(content);
			send("1", "작성자> " + board.getWriter() + "\n작성자명(입력 안할 시 수정 안 됨)> ");
			String writer = in.readUTF();
			if (writer.equals("")) {
				writer = board.getWriter();
			}
			board.setWriter(writer);
			board.setCreatedDate(board.getCreatedDate());
		} catch (NullPointerException e) {
			System.out.println("자료가 없습니다.");
			return;
		} catch (IOException e) {

		}
		boardService.updateOne(board);
		try {
			send("0", "게시글 수정 완료.");
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

	public void send(String occupy, String command) throws IOException {
		out.writeUTF(occupy);
		out.writeUTF(command);
		out.flush();
	}

	public static boolean isInteger(String strValue) {
		try {
			Integer.parseInt(strValue);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	public String addSpace(String std, String insertStr) {
		StringBuilder sb = new StringBuilder();
		sb.append(" ");
		String tmp = insertStr;
		if (insertStr.length() >= std.length()) {
			tmp = insertStr.substring(0, std.length() - 4);
			tmp += "...";
		}
		sb.append(tmp);
		for (int i = tmp.length(); i < std.length(); i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

	public String addSpace(String std, int num) {
		String insertStr = String.valueOf(num);
		return addSpace(std, insertStr);
	}
	
	public int isAscii(char charValue) {
		try {
			if( charValue >= 128)
				return 2;
		} catch (NumberFormatException ex) {
		}
		return 1;
	}
	
	public int toAsciiLength(String str) {
		int length = 0;
		for(int i =0;i<str.length();i++) {
			length += isAscii(str.charAt(i));
		}
		return length;
	}
	
	public int toAsciiLength(int integer) {
		String insertStr = String.valueOf(integer);
		return toAsciiLength(insertStr);
	}

	public void defaultMethod() throws IOException {
		send("0", "메뉴를 입력하세요.");
	}

}
