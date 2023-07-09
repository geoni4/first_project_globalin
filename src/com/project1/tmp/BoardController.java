package com.project1.tmp;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class BoardController {
	private BoardService boardService = null;
	
	
	
	public BoardController() {
		boardService = new BoardService();
	}

	public void getList() {
		
		List<Board> boardList = boardService.findAll();
		
		System.out.println("글번호 | \t 제목 \t | \t 작성자 \t | \t 작성 날짜 \t");
		for(Board board: boardList) {
			System.out.print(board.getBno() + "\t");
			System.out.print(board.getTitle()+  "\t");
			System.out.print(board.getWriter()+  "\t");
			System.out.print(board.getCreateDate()+  "\t");
			System.out.println();
		}
		
	}
	
	public void createContent() {
		Scanner scanner = new Scanner(System.in);
		
		Board board = new Board();
		try {
			System.out.print("제목> ");
			board.setTitle(scanner.nextLine());
			System.out.print("내용> ");
			board.setContent(scanner.nextLine());
			System.out.print("작성자> ");
			board.setWriter(scanner.nextLine());
		} catch (Exception e) {
			System.out.println();
			System.out.println("==============");	
			System.out.println("controller 오류");
			System.out.println("--------------");
			System.out.println("제대로 입력하세요.");
			System.out.println("==============");	
			return;
		}
		boardService.insertOne(board);
		
	}
	
	public void getDetailContent() {
	
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("내용을 확인할 글 번호를 입력하세요> ");
		int bno;
		try {
			bno = scanner.nextInt();
		}catch (Exception e) {
			System.out.println();
			System.out.println("==============");	
			System.out.println("controller 오류");
			System.out.println("--------------");
			System.out.println("번호를 입력하세요.");
			System.out.println("==============");
			System.out.println();
			return ;
		}
		scanner.nextLine();
		Board board = null;
		try {
			board = boardService.findOne(bno);
			System.out.println("글 번호 : " + board.getBno());
			System.out.println("제목 : " + board.getTitle());
			System.out.println("내용 : " + board.getContent());
			System.out.println("작성자 : " + board.getWriter());
		} catch(NullPointerException e) {

		} 
	}

	
	public void deleteContent() {
		Scanner scanner = new Scanner(System.in);
		
		int bno = 0;
		System.out.print("삭제할 글 번호를 입력하세요> ");
		try {
			bno = scanner.nextInt();
		} catch (Exception e) {
			System.out.println("번호를 입력하세요.");
			return;
		}
		if(boardService.deleteOne(bno)) 
			System.out.println("글이 삭제되었습니다.");
		else
			System.out.println("글이 없습니다.");
		
	}

	
	public void modifyContent() {
		
	}
	
}
