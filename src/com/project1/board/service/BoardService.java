package com.project1.board.service;

import java.util.List;

import com.project1.board.DAO.BoardDAO;
import com.project1.board.DO.Board;

public class BoardService {
	private BoardDAO dao =null;
	
	
	public BoardService() {
		dao = new BoardDAO();
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
