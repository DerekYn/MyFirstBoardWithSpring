package com.spring.board.model;

import java.util.HashMap;
import java.util.List;

import com.spring.member.model.MemberVO;

// ==== model단(DAO)의 인터페이스 생성 ====
public interface InterBoardDAO {

	List<String> getImgfilenameList(); 							// 이미지 파일명 가져오기

	MemberVO getLoginMemeber(HashMap<String, String> map); 		// 로그인 여부 알아오기 

	int add(BoardVO boardvo); 									// 글쓰기(파일첨부가 없는 글쓰기)

	List<BoardVO> boardList(); 									// 글목록 보여주기(검색어가 없는 전체글목록 보여주기)

	BoardVO getView(String seq);								// 글 1개를 보여주기

	void setAddReadCount(String seq); 							// 글 1개를 볼때 조회수(readCount) 1증가 시키기 

	boolean checkPw(HashMap<String,String> map); 				// 글수정 및 글삭제시 암호일치 여부 알아오기

	int updateContent(BoardVO boardvo); 						// 글 1개 수정하기

	int deleteContent(HashMap<String, String> map); 			// 글 1개 삭제하기

	int addComment(CommentVO commentvo); 						// 댓글쓰기
	int updateCommentCount(String parentSeq); 					// 댓글쓰기 이후에 댓글의 갯수(commentCount 컬럼) 1증가 시키기  

	List<CommentVO> listComment(String seq); 					// 댓글내용 보여주기

	boolean isExistsComment(HashMap<String, String> map); 		// 원게시글에 딸린 댓글이 있는지 없는지를 확인하기  
	int deleteComment(HashMap<String, String> map); 			// 원게시글에 딸린 댓글들 삭제하기

	List<BoardVO> boardList(HashMap<String, String> map); 		// 글목록 보여주기(검색어가 없는 것)
	List<BoardVO> boardList2(HashMap<String, String> map);		// 글목록 보여주기(검색어가 있는 것)

	int getTotalCount(); 										// 검색어가 없는 총게시물 건수
	int getTotalCount2(HashMap<String, String> map); 			// 검색어가 있는 총게시물 건수

	int getGroupMaxno();										// tblBoard 테이블의 groupno 의 max값 알아오기

	int add_withFile(BoardVO boardvo);							// 파일첨부가 있는 글쓰기
	
	
	
	

}
