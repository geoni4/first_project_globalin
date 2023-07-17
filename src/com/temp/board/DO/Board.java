package com.temp.board.DO;

public class Board {
	private int bno;
	private String title, content, writer, createdDate, modifiedDate;

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
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	public Board() {}
	
	public Board(int bno, String title, String content, String writer, String createdDate, String modifiedDate) {
		this.bno = bno;
		this.title = title;
		this.content = content;
		this.writer = writer;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}
	
	@Override
	public String toString() {
		return bno + "\t" + title + '\t' + writer + '\t' + createdDate + '\t' + modifiedDate ;
	}
}
