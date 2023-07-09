package com.project1.tmp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BoardDAO {
	private BufferedReader br = null;
	private BufferedWriter bw = null;
	
	public List<Board> getAll(){
		int bno = 1;
		List<Board> boardList = new ArrayList<>();
		
		try {
			br = new BufferedReader(new FileReader("./project/bno.txt"));
			bno = Integer.valueOf(br.readLine());
			br.close();
			for(int no = 1 ; no <= bno ; no++) {
				Board board = getOne(no);
				if(board == null) continue;
				boardList.add(board);
			}
		} catch (Exception e) {
			System.out.println("자료가 없습니다.");
			boardList = null;
		}
		return boardList;
	}
	
	public void insert(Board board) {
		LocalDateTime localDateTime = null;
		int bno =0;
		try {
			br= new BufferedReader(new FileReader("./project/bno.txt"));
			bno = Integer.valueOf(br.readLine());
			br.close();
		} catch (Exception e) {
		}
		localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now());
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy, h:MM:ss a");
		String date = localDateTime.format(dtf);
	    try {
	    	bw= new BufferedWriter(new FileWriter("./project/bno.txt", false));
	    	bw.write(String.valueOf(++bno));
	    	bw.close();
	    	String writeFileName = "./project/board/" + bno + ".txt";
	    	bw = new BufferedWriter(new FileWriter(writeFileName));
	    	String bnoStr = String.valueOf(bno);
	    	bw.write(bnoStr, 0, bnoStr.length());
	    	
	    	bw.newLine();
	    	bw.write(board.getTitle(), 0, board.getTitle().length());
	    	bw.newLine();
	    	bw.write(board.getContent(), 0, board.getContent().length());
	    	bw.newLine();
	    	bw.write(board.getWriter(), 0, board.getWriter().length());
	    	bw.newLine();
	    	bw.write(date, 0, date.length());
	    	bw.newLine();
	    	bw.write(date, 0, date.length());
	    	bw.newLine();
	    	bw.close();
	    } catch(Exception e) {
	    	System.out.println();
	    	System.out.println("예기치 않은 오류가 발생했습니다.");
	    	System.out.println();
	    	return;
	    }

		System.out.println("글 작성이 완료되었습니다.");
	}
	
	public Board getOne(int bno) {
		
		Board board =null;
		try {
			br = new BufferedReader(new FileReader("./project/bno.txt"));
			String bnoStr;
			bnoStr = br.readLine();
			br.close();
			
			if(bnoStr == null) throw new NullPointerException();
			String readFileName = "./project/board/"+bno+".txt";
			br = new BufferedReader(new FileReader(readFileName));
			
			board = new Board();
			
			board.setBno(Integer.valueOf(br.readLine()));
			board.setTitle(br.readLine());
			board.setContent(br.readLine());
			board.setWriter(br.readLine());
			board.setCreateDate(br.readLine());
			board.setModifyDate(br.readLine());
			
			br.close();
		} catch(FileNotFoundException e) {
			
		} catch(IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println();
			System.out.println("자료가 없습니다.");
			System.out.println();
		}
		
		return board;
	}
	
	public boolean deleteOne(int bno) {
		try {
			br = new BufferedReader(new FileReader("./project/bno.txt"));
			String bnoStr;
			bnoStr = br.readLine();
			br.close();
			if(bnoStr == null) throw new NullPointerException();
			String deleteFileName = "./project/board/" +bno + ".txt";
			File file = new File(deleteFileName);
			return file.delete();
		} catch (Exception e) {
		}
		return true;
	}
	
}
