package com.project1.board.DO;

public class Board {
	private int bno;
	private String title, content, writer, createDate, modifyDate;

	public int getBno() {
		return bno;
	}
	public void setBno(int bno) {
		this.bno = bno;
	}

	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	public Board() {}
	
	public Board(int bno, String title, String content, String writer, String createDate, String modifyDate) {
		this.bno = bno;
		this.title = title;
		this.content = content;
		this.writer = writer;
		this.createDate = createDate;
		this.modifyDate = modifyDate;
	}
	@Override
	public String toString() {
		return bno + "\t" + title + '\t' + writer + '\t' + createDate + '\t' + modifyDate ;
	}
	
	
}
