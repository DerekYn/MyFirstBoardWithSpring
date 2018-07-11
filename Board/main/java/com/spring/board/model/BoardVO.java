package com.spring.board.model;

import org.springframework.web.multipart.MultipartFile;

// ===== #53-1. VO �����ϱ� =====
public class BoardVO {

	private String seq;         // �۹�ȣ
	private String userid;      // �����ID
	private String name;        // �۾���
	private String subject;     // ������
	private String content;     // �۳���   
	private String pw;          // �۾�ȣ
	private String readCount;   // ����ȸ��
	private String regDate;     // �۾��ð�
	private String status;      // �ۻ�������  1:��밡���ѱ�,  0:�����ȱ� 

	// ==== #83. commentCount ������Ƽ �߰��ϱ�
	//           ���� tblBoard ���̺� commentCount �÷��� �߰��� ������ �ؾ� �Ѵ�. ====
	private String commentCount; // ��ۼ�

	
	// ==== #118. �亯�� �Խ����� ���� ������� �߰��ϱ� 
	//            ���� ����Ŭ���� tblBoard ���̺�� tblComment ���̺��� ���� ���� ������ ��
	//			    ������ ���� ������ �Ʒ�ó�� �ؾ� �Ѵ�.

	private String groupno;
/*
	�亯�۾��⿡ �־ �׷��ȣ
	����(�θ��)�� �亯���� ������ groupno �� ������. 
	�亯���� �ƴ� ����(�θ��)�� ��� groupno �� ���� groupno �÷��� �ִ밪(max)+1 �� �Ѵ�.  
 */
	private String fk_seq;
/*	fk_seq �÷��� ����� foreign key�� �ƴϴ�.
    fk_seq �÷��� �ڽ��� ��(�亯��)�� �־ 
	����(�θ��)�� ���������� ���� �������̴�.
	�亯�۾��⿡ �־ �亯���̶�� fk_seq �÷��� ���� 
	����(�θ��)�� seq �÷��� ���� ������ �Ǹ�,
	�亯���� �ƴ� ������ ��� 0 �� �������� �Ѵ�.
*/
	
	private String depthno;
/*
	�亯�۾��⿡ �־ �亯�� �̶��                                                
	����(�θ��)�� depthno + 1 �� ������ �Ǹ�,
	�亯���� �ƴ� ������ ��� 0 �� �������� �Ѵ�.
*/
	
/*
	==== #133. ����÷�θ� �ϵ��� VO �����ϱ� ====
		  ����, ����Ŭ���� tblBoard ���̺� 3�� �÷�(fileName, orgFilename, fileSize)�� �߰��� ������
		  �Ʒ��� �۾��� �ؾ� �Ѵ�.
*/
	private String fileName;		// WAS(��Ĺ)�� ����� ���ϸ�(20161121324325454354353333432.png)
	private String orgFilename;		// ��¥ ���ϸ�(������.png) // ����ڰ� ������ ���ε� �ϰų� ������ �ٿ�ε� �Ҷ� ���Ǿ����� ���ϸ�
	private String fileSize;		// ����ũ��
	
	private MultipartFile attach;	// ��¥ ���� ==> WAS(��Ĺ) ��ũ�� �����.
	// !!!!! MultipartFile attach �� ����Ŭ �����ͺ��̽� tblBoard ���̺��� �÷��� �ƴϴ� !!!!!
	
	public BoardVO() { }
	
	public BoardVO(String seq, String userid, String name, String subject, String content, String pw, String readCount
			,String regDate, String status
			,String commentCount
			,String groupno, String fk_seq, String depthno
			,String fileName, String orgFilename, String fileSize) {
		this.seq = seq;
		this.userid = userid;
		this.name = name;
		this.subject = subject;
		this.content = content;
		this.pw = pw;
		this.readCount = readCount;
		this.regDate = regDate;
		this.status = status;
		
		this.commentCount = commentCount;
		
		this.groupno = groupno;
		this.fk_seq = fk_seq;	
		this.depthno = depthno;
		
		this.fileName = fileName;
		this.orgFilename = orgFilename;
		this.fileSize = fileSize;			
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getReadCount() {
		return readCount;
	}

	public void setReadCount(String readCount) {
		this.readCount = readCount;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}

	public String getGroupno() {
		return groupno;
	}

	public void setGroupno(String groupno) {
		this.groupno = groupno;
	}

	public String getFk_seq() {
		return fk_seq;
	}

	public void setFk_seq(String fk_seq) {
		this.fk_seq = fk_seq;
	}

	public String getDepthno() {
		return depthno;
	}

	public void setDepthno(String depthno) {
		this.depthno = depthno;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getOrgFilename() {
		return orgFilename;
	}

	public void setOrgFilename(String orgFilename) {
		this.orgFilename = orgFilename;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public MultipartFile getAttach() {
		return attach;
	}

	public void setAttach(MultipartFile attach) {
		this.attach = attach;
	}
	
	
	
}
