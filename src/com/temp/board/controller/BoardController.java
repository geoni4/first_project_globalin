package com.temp.board.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import com.temp.board.DO.Board;
import com.temp.board.service.BoardService;

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

		String occupy = "1";
		try {
			while (true) {
				int allPage = (boardService.findAll().size()-1)/5+1;
				List<Board> boardList = boardService.findPartailinPage(page);
				StringBuilder tmp = new StringBuilder();
				String bnoStr = " 글번호     ";
				String titleStr = "  제목                ";
				String writerStr = "  작성자        ";
				String createdDateStr = " 작성 날짜                          ";
				tmp.append("===============================================================================================\n")
					.append("|").append(bnoStr).append(" |")
					.append(titleStr).append(" |")
					.append(writerStr).append(" |")
					.append(createdDateStr).append(" |\n")
					.append("|-------------|-----------------------|-----------------|-------------------------------------|\n");
				if (boardList == null || boardList.size() == 0) {
					tmp = new StringBuilder().append("글이 없습니다.");
					occupy = "0";
				}
				if (boardList != null) {
					for (Board board : boardList) {
						tmp.append("|").append(addSpace(bnoStr, board.getBno())).append("|")
								.append(addSpace(titleStr, board.getTitle())).append("|")
								.append(addSpace(writerStr, board.getWriter())).append("|")
								.append(addSpace(createdDateStr, board.getCreatedDate())).append("|\n");
					}
					tmp.append("===============================================================================================\n")
						.append("\n현재 페이지 : ").append(page).append(", ").append(page).append(" / ")
						.append(allPage).append(" |  총 글 개수: " + boardService.findAll().size()).append("\n\n")
						.append("명령어를 입력하세요.\n")
						.append("(<: 왼쪽 페이지, >: 오른쪽 페이지, <<: 첫 페이지, >>: 끝 페이지)\n")
						.append("(숫자: 해당 페이지, 0: 목록 출력 종료)");
				}
				String summaryString = tmp.toString();
				if(occupy.equals("0")) {
					send(occupy, "");
					break;
				}
				send(occupy, summaryString);
				String cmd = in.readUTF();

				page = partialListMenu(cmd, allPage, page);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int partialListMenu(String cmd, int allPage, int page) {
		int tmpPage = page;
		if (">".equals(cmd))	tmpPage = tmpPage+1 >= allPage ? allPage : tmpPage+1;
		if ("<".equals(cmd))	tmpPage = tmpPage <= 1 ? 1 : tmpPage-1;
		if(">>".equals(cmd))	tmpPage = allPage;
		if("<<".equals(cmd))	tmpPage = 1;
		if (isInteger(cmd)) {
			tmpPage = (0 <= Integer.valueOf(cmd)) && (Integer.valueOf(cmd) <= allPage) ? Integer.valueOf(cmd) : tmpPage;
		}
		return tmpPage;
	}

	
	
	
	
	public void getList() {
		List<Board> boardList = boardService.findAll();
		try {
			StringBuilder tmp = new StringBuilder();
			String bnoStr = "글번호     ";
			String titleStr = "  제목                ";
			String writerStr = "  작성자        ";
			String createdDateStr = " 작성 날짜                          ";
			tmp.append(" ").append(bnoStr).append("|").append(titleStr).append("|").append(writerStr).append("|")
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

	
	
	
	
	public void defaultMethod() throws IOException {
		send("0", "메뉴를 입력하세요.");
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
		String tmp = insertStr;
		sb.append(" ");
		if(toAsciiLength(std) <= toAsciiLength(insertStr)) {
			tmp = insertStr.substring(0, getIdx(std, insertStr));
			tmp += "...";
		}
		sb.append(tmp);
		for (int i = toAsciiLength(tmp); i < toAsciiLength(std); i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

	public String addSpace(String std, int num) {
		String insertStr = String.valueOf(num);
		return addSpace(std, insertStr);
	}
	

	

	public int isAscii(char charValue) {
		if (charValue >= 128) {
			return 4;
		}
		return 2;
	}
	
	
	public int getIdx(String std, String insertStr) {
		int idx=0;
		int doubleLen = 0 ;
		for(int i =0;i<insertStr.length();i++) {
			doubleLen += isAscii(insertStr.charAt(i));
			idx = i;
			if(doubleLen/2 >= toAsciiLength(std) -4) break;
		}
		return idx;
	}

	
	public int toAsciiLength(String str) {
		int doubleLen = 0;
		for(int i =0;i<str.length();i++) {
			doubleLen += isAscii(str.charAt(i));
		}
		int length = doubleLen/2;
		return length;
	}
	
	public int toAsciiLength(int integer) {
		String intStr = String.valueOf(integer);
		return toAsciiLength(intStr);
	}


}
