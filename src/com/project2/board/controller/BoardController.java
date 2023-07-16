package com.project2.board.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import org.json.JSONObject;

import com.project2.board.DO.Board;
import com.project2.board.service.BoardService;

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
		}
	}

	
	
	
	
	public void getPartialList() {
		int page = 1;

		String command = "1";
		try {
			while (true) {
				List<Board> allBoardList = boardService.findAll();
				List<Board> boardList = boardService.findPartialinPage(page);
				
				String position = "in-menu";
				StringBuilder tmp = new StringBuilder();
				String bnoStr = " 글번호     ";
				String titleStr = "  제목                ";
				String writerStr = "  작성자        ";
				String createdDateStr = " 작성 날짜                          ";
				int allPage = 1;
				if (page==0) {
					tmp = new StringBuilder().append("메뉴로 이동합니다.");
					command = "0";
				} else if(allBoardList == null ) {
					tmp = new StringBuilder().append("글이 없습니다.");
					command = "0";
				} else {
					int allPost = allBoardList.size();
					allPage= (allPost-1)/5+1;
					tmp.append("===============================================================================================\n")
						.append("|").append(bnoStr).append(" |")
						.append(titleStr).append(" |")
						.append(writerStr).append(" |")
						.append(createdDateStr).append(" |\n")
						.append("|-------------|-----------------------|-----------------|-------------------------------------|\n");
					for (Board board : boardList) {
						tmp.append("|").append(addSpace(bnoStr, board.getBno())).append("|")
								.append(addSpace(titleStr, board.getTitle())).append("|")
								.append(addSpace(writerStr, board.getWriter())).append("|")
								.append(addSpace(createdDateStr, board.getCreatedDate())).append("|\n");
					}
					tmp.append("===============================================================================================\n")
						.append("\n현재 페이지 : ").append(page).append(", ").append(page).append(" / ")
						.append(allPage).append(" |  총 글 개수: " + allPost).append("\n\n")
						.append("명령어를 입력하세요.\n")
						.append("(<: 왼쪽 페이지, >: 오른쪽 페이지, <<: 첫 페이지, >>: 끝 페이지)\n")
						.append("(숫자: 해당 페이지, 0: 목록 출력 종료)");
				}
				String summaryString = tmp.toString();
				
				JSONObject jsonObject = new JSONObject()
						.put("position", position)
						.put("data",summaryString);
				if("0".equals(command)) {
					defaultMenu(jsonObject);
					return;
				}
				send(jsonObject);
				
				command = receiveCommand();

				page = partialListMenu(command, allPage, page);
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

	
	
	
	public void createContent() {
		Board board = new Board();
		try {
			JSONObject jsonObject = new JSONObject()
					.put("position", "in-menu")
					.put("data", "제목> ");
			send(jsonObject);
			board.setTitle( receiveCommand() );
			
			jsonObject.put("data", "내용> ");
			send(jsonObject);
			board.setContent( receiveCommand() );
			
			jsonObject.put("data", "작성자> ");
			send(jsonObject);
			board.setWriter( receiveCommand() );
		} catch (Exception e) {
			String message = "글 작성 중 오류 발생.";
			defaultMenu(backToMenu(message));
			return;
		}
		boardService.insertOne(board);
		String message = "글이 등록되었습니다.";
		defaultMenu(backToMenu(message));
	}

	

	
	
	public void getDetailContent() {
		int bno;
		try {
			JSONObject jsonObject = new JSONObject()
						.put("position", "in-menu")
						.put("data", "내용을 확인할 글 번호를 입력하세요> ");
			send(jsonObject);
			bno = Integer.valueOf(receiveCommand());
		} catch (Exception e) {
			String message = "글 없음.";
			defaultMenu( backToMenu(message) );
			return;
		}
		Board board;
		String message = "글 없음.";
		board = boardService.findOne(bno);
		if (board != null) {
			message = new StringBuilder()
					.append("글 번호 : ").append(board.getBno()).append("\n")
					.append("제목 : ").append(board.getTitle()).append("\n")
					.append("내용 : ").append(board.getContent()).append("\n")
					.append("작성자 : ").append(board.getWriter()).append("\n")
					.append("작성일자 : ").append(board.getCreatedDate()).append("\n")
					.append("수정일자 : ").append(board.getModifiedDate()).append("\n")
					.toString();
		}
		defaultMenu( backToMenu(message) );
	}

	
	
	
	
	public void deleteContent() {
		int bno = 0;
		try {
			JSONObject jsonObject = new JSONObject()
						.put("position", "in-menu")
						.put("data", "삭제할 글 번호를 입력하세요> ");
			send(jsonObject);
			bno = Integer.valueOf( receiveCommand() );
		} catch (Exception e) {
			String message = "글이 없습니다.";
			defaultMenu(backToMenu(message));
			return;
		}
		String message = "글이 삭제되었습니다.";
		if (boardService.deleteOne(bno)) {
			message = "글이 삭제되었습니다.";
		} else {
			message = "글이 없습니다.";
		}
		defaultMenu(backToMenu(message));
	}

	
	
	
	
	public void modifyContent() {
		int bno = 0;
		try {
			JSONObject jsonObject = new JSONObject()
					.put("position", "in-menu")
					.put("data", "수정할 글 번호를 입력하세요> ");
			send(jsonObject);
			bno = Integer.valueOf(new JSONObject(in.readUTF()).get("command").toString());
		} catch (Exception e) {
			return;
		}
		Board board = null;
		String message = "글 없음.";
		try {
			board = boardService.findOne(bno);
			if (board != null) {
				message = new StringBuilder()
						.append("글 번호 : ").append(board.getBno()).append("\n")
						.append("제목 : ").append(board.getTitle()).append("\n")
						.append("내용 : ").append(board.getContent()).append("\n")
						.append("작성자 : ").append(board.getWriter()).append("\n")
						.append("작성일자 : ")	.append(board.getCreatedDate()).append("\n")
						.append("수정일자 : ").append(board.getModifiedDate())	.append("\n")
						.append("수정하시겠습니까? (Y/N)").toString();
			} else {
				defaultMenu(backToMenu(message));
				return;
			}
			JSONObject jsonObject = new JSONObject()
					.put("position", "in-menu")
					.put("data", message);
			send(jsonObject);
			String menu = receiveCommand();
			if (!"y".equalsIgnoreCase(menu)) {
				message = "수정 취소.";
				defaultMenu( backToMenu(message));
				return;
			}
			
			message = "제목> " + board.getTitle() + "\n바꿀 제목(입력 안할 시 수정 안 됨)> ";
			jsonObject.put("data", message);
			send(jsonObject);
			String title = receiveCommand();
			if (title.equals("")) {
				title = board.getTitle();
			}
			board.setTitle(title);
			
			message =  "내용> " + board.getContent() + "\n바꿀 내용(입력 안할 시 수정 안 됨)> ";
			jsonObject.put("data", message);
			send(jsonObject);
			String content = receiveCommand();
			if (content.equals("")) {
				content = board.getContent();
			}
			board.setContent(content);
			
			message = "작성자> " + board.getWriter() + "\n작성자명(입력 안할 시 수정 안 됨)> ";
			jsonObject.put("data", message);
			send(jsonObject);
			String writer = receiveCommand();
			if (writer.equals("")) {
				writer = board.getWriter();
			}
			board.setWriter(writer);
			
			board.setCreatedDate(board.getCreatedDate());
			
		} catch (NullPointerException e) {
			message = "자료가 없습니다.";
			defaultMenu(backToMenu(message));
			return;
		} catch (IOException e) {

		}
		boardService.updateOne(board);
		message = "게시글 수정 완료.";
		defaultMenu(backToMenu(message));
	}

	
	public void defaultMenu() {
		defaultMenu(backToMenu(""));
	}
	
	
	public void defaultMenu(JSONObject jsonObject) {
		System.out.println("메인 메뉴");
		String data = "\n메뉴를 입력하세요.\n1. 목록  2. 등록  3. 내용  4. 삭제  5. 수정  0. 종료 > ";
		jsonObject.put("position","main")
				.put("command","")
				.put("data", jsonObject.getString("data")+data);
		send(jsonObject);
	}
	
	public JSONObject backToMenu(String message) {
		return new JSONObject().put("data", message);
	}
	
	public String receiveCommand() throws IOException {
		return new JSONObject(in.readUTF()).getString("command");
	}
	
	public void close() {
		try {
			out.close();
			in.close();
		} catch (Exception e) {

		}
	}

	public void send(JSONObject json){
		try {
			out.writeUTF(json.toString());
			out.flush();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}//send()
	
	
	
	
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
