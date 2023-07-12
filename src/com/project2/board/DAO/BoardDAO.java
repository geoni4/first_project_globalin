package com.project2.board.DAO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.project1.board.DO.Board;


public class BoardDAO {
	private String pPathString = "./project/";
	private String bPathString = pPathString+"/board/";
	private String bnoPathString = pPathString+"bno.txt";
	private String bcntPathString = pPathString + "bcount.txt";
	private BufferedReader br = null;
	private BufferedWriter bw = null;
	
	
	public BoardDAO() {
		Path projectPath = Paths.get(pPathString);
		Path boardPath = Paths.get(bPathString);
		Path bnoPath = Paths.get(bnoPathString);
		Path bcntPath = Paths.get(bcntPathString);
		try {
			if(!Files.exists(projectPath))	Files.createDirectory(projectPath);
			if(!Files.exists(boardPath))	Files.createDirectory(boardPath);
			if(!Files.exists(bnoPath))		{
		    	bw = new BufferedWriter(new FileWriter(bnoPathString, false));
		    	bw.write(String.valueOf(0));
		    	bw.close();
			}
			if(!Files.exists(bcntPath)) {
				bw = new BufferedWriter(new FileWriter(bcntPathString, false));
		    	bw.write(String.valueOf(0));
		    	bw.close();
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Board> getAllinPage(int page){
		int bno = 0;
		List<Board> boardList = new ArrayList<>();
		try {
			br = new BufferedReader(new FileReader(bnoPathString));
			bno = Integer.valueOf(br.readLine());
		} catch (Exception e) {
			System.out.println("bno.txt 파일 이상 발생");
			return boardList;
		} finally {
			try {
			br.close();
			} catch (Exception e) {
			}
		}
		
		try {
			List<Board> boardTmpList = new ArrayList<>();
			for(int no=bno;no >= 1;no--) {
				if(page*5 <= boardTmpList.size()) break;
				Board board = getOne(no);
				if(board == null ) continue;
				boardTmpList.add(board);
			}
			for(int no =(page-1)*5 ; no<boardTmpList.size();no++) {
				boardList.add(boardTmpList.get(no));
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return boardList;
	}
	
	
	public List<Board> getAll(){
		int bno = 0;
		List<Board> boardList = new ArrayList<>();
		try {
			br = new BufferedReader(new FileReader(bnoPathString));
			bno = Integer.valueOf(br.readLine());
			br.close();
		} catch (Exception e) {
			System.out.println("bno.txt 파일 이상 발생");
			return boardList;
		} 
		try {

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
		int bno =0, bcnt=0;
		try {
			br= new BufferedReader(new FileReader(bnoPathString));
			bno = Integer.valueOf(br.readLine());
			br.close();
			br = new BufferedReader(new FileReader(bcntPathString));
			bcnt = Integer.valueOf(br.readLine());
			br.close();
		} catch (Exception e) {
			System.out.println("bno.txt, bcnt.txt 파일 이상 발생");
			return;
		}
		localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now());
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy, h:mm:ss a");
		String date = localDateTime.format(dtf);
	    try {
	    	bw = new BufferedWriter(new FileWriter(bnoPathString));
	    	bw.write(String.valueOf(++bno));
	    	bw.close();
	    	
	    	String writeFileName = bPathString + bno + ".txt";
	    	StringBuilder tmp = new StringBuilder();
	    	tmp.append(bno).append('\n')
	    		.append(board.getTitle()).append('\n')
	    		.append(board.getContent()).append('\n')
	    		.append(board.getWriter()).append('\n')
	    		.append(date).append('\n')
	    		.append(date).append('\n');
	    	bw = new BufferedWriter(new FileWriter(writeFileName));
	    	bw.write(tmp.toString());
	    	bw.close();
	    	
	    	bw = new BufferedWriter(new FileWriter(bcntPathString));
	    	bw.write(String.valueOf(++bcnt));
	    	bw.close();
	    	
	    } catch(Exception e) {
	    	System.out.println("insert 오류");
	    	return;
	    }

		System.out.println("글 작성이 완료되었습니다.");
	}
	
	public Board getOne(int bno) {
		
		Board board =null;
		try {
			String readFileName = bPathString+bno+".txt";
			br = new BufferedReader(new FileReader(readFileName));
			
			board = new Board();
			
			board.setBno(Integer.valueOf(br.readLine()));
			board.setTitle(br.readLine());
			board.setContent(br.readLine());
			board.setWriter(br.readLine());
			board.setCreatedDate(br.readLine());
			board.setModifiedDate(br.readLine());
			
			br.close();
		} catch(FileNotFoundException e) {
			//System.out.println("파일이 없습니다.");
		} catch(IOException e) {
			System.out.println("파일 IO오류");
		} catch (NullPointerException e) {
			System.out.println("자료가 없습니다.");
		}
		
		return board;
	}
	
	public boolean deleteOne(int bno) {
		int bcnt =0;
		try {
			br = new BufferedReader(new FileReader(bcntPathString));
	    	bcnt = Integer.valueOf(br.readLine());
		} catch (Exception e) {
			System.out.println("cnt.txt 접근 실패");
		} finally {
			try {
			br.close();
			} catch (Exception e) {
			}
		}
		
		try {
			if(bcnt == 0) throw new Exception();
	    	bw = new BufferedWriter(new FileWriter(bcntPathString));
	    	bw.write(String.valueOf(--bcnt));
			String deleteFileName = bPathString +bno + ".txt";
			File file = new File(deleteFileName);
			return file.delete();
		} catch (Exception e) {
			System.out.println("자료 접근 실패");
			return false;
		} finally {
			try {
				bw.close();
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	public void update(Board board) {
		LocalDateTime localDateTime = null;
		localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now());
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy, h:mm:ss a");
		String date = localDateTime.format(dtf);

		
	    try {
	    	String writeFileName = bPathString + board.getBno() + ".txt";
	    	StringBuilder tmp = new StringBuilder();
	    	tmp.append(board.getBno()).append('\n')
	    		.append(board.getTitle()).append('\n')
	    		.append(board.getContent()).append('\n')
	    		.append(board.getWriter()).append('\n')
	    		.append(board.getCreatedDate()).append('\n')
	    		.append(date).append('\n');
	    	bw = new BufferedWriter(new FileWriter(writeFileName));
	    	bw.write(tmp.toString());
	    	bw.close();
	    	
	    } catch(Exception e) {
	    	System.out.println("update 오류");
	    	return;
	    }
		System.out.println("글 수정이 완료되었습니다.");
	}
}
