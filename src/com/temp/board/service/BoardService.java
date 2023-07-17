package com.temp.board.service;

import java.util.List;

import com.temp.board.DAO.BoardDAO;
import com.temp.board.DO.Board;

public class BoardService {
	private BoardDAO dao =null;
	
	
	public BoardService() {
		dao = new BoardDAO();
	}
	
	public List<Board> findPartailinPage(int page){
		return dao.getAllinPage(page);
	}

	public List<Board> findAll(){
		return dao.getAll();
	}
	
	
	public void insertOne(Board board) {
		dao.insert(board);
	}
	
	public Board findOne(int num) {
		return dao.getOne(num);
	}
	
	public boolean deleteOne(int num){
		return dao.deleteOne(num);
	}

	public void updateOne(Board board) {
		dao.update(board);
		
	}
	
	
	
}
