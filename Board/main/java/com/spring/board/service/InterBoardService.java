package com.spring.board.service;

import java.util.HashMap;
import java.util.List;

import com.spring.board.model.BoardVO;
import com.spring.board.model.CommentVO;
import com.spring.member.model.MemberVO;

// === Service단 인터페이스 선언 ===
public interface InterBoardService {

	List<String> getImgfilenameList(); 								// 이미지 파일명 가져오기

	MemberVO getLoginMemeber(HashMap<String, String> map); 			// 로그인 여부 알아오기   

	int add(BoardVO boardvo); 										// 글쓰기(파일첨부가 없는 글쓰기)

	List<BoardVO> boardList(); 										// 글목록 보여주기(검색어가 없는 전체글목록 보여주기) 

	BoardVO getView(String seq, String userid); 					// 조회수(readCount) 증가한 후에 글1개를 가져오는 것.
	// BoardVO가 리턴타입으면 글 1개를 가져오는것
	
	// 단, 자신이 쓴 글을 자신이 읽을시에는 조회수가 증가되지 않고,
	// 다른 사람이 쓴 글이라야 조회수가 증가되도록 해야 한다.
	// 로그인 하지 않은 상태에서 글을 읽을때 조회수 증가가 일어나지 않도록 한다. 

	BoardVO getViewWithNoReadCount(String seq); 					// 조회수 1증가 없이 그냥 글 1개를 가져오는 것

	int edit(BoardVO boardvo); 										// 1개글 수정하기 

//	int del(HashMap<String, String> map); 							// 1개글 삭제하기
	int del(HashMap<String, String> map) throws Throwable; 			// 1개글 삭제하기

	int addComment(CommentVO commentvo) throws Throwable; 			// 댓글쓰기 
	
	List<CommentVO> listComment(String seq); 						// 댓글내용 보여주기

	List<BoardVO> boardList(HashMap<String, String> map);  			// 글목록 보여주기(검색어가 없는 것)
	List<BoardVO> boardList2(HashMap<String, String> map); 			// 글목록 보여주기(검색어가 있는 것)   

	int getTotalCount();  											// 검색어가 없는 총게시물 건수
	int getTotalCount2(HashMap<String, String> map); 				// 검색어가 있는 총게시물 건수

	int add_withFile(BoardVO boardvo);								// 파일 첨부가 있는 글쓰기
	
	      
	
	
	
	
	

}
