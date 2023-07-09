package com.project1.tmp;

import java.util.List;

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
	
}
