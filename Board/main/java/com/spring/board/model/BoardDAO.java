package com.spring.board.model;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.member.model.MemberVO;

// ===== #28. DAO 선언 =====
@Repository
public class BoardDAO implements InterBoardDAO {

	// ===== #29. 의존객체 주입하기(DI : Dependency Injection) =====
	@Autowired
	private SqlSessionTemplate sqlsession;
	
	
	// ==== #42. 메인페이지용 이미지 파일이름을 가져오는 메소드 생성하기 ====
	@Override
	public List<String> getImgfilenameList() {
		List<String> list = sqlsession.selectList("board.getImgfilenameList");
		return list;
	}


	// ===== #47. 로그인 여부 알아오기 =====
	@Override
	public MemberVO getLoginMemeber(HashMap<String, String> map) {
		MemberVO loginuser = sqlsession.selectOne("board.getLoginMemeber", map);
		return loginuser;
	}


	// ==== #55. 글쓰기(파일첨부가 없는 글쓰기) ====
	@Override
	public int add(BoardVO boardvo) {
		int n = sqlsession.insert("board.add", boardvo);
		return n;
	}

    // ==== #59. 글목록 보여주기(검색어가 없는 전체글목록 보여주기) ====
	@Override
	public List<BoardVO> boardList() {
		List<BoardVO> boardList = sqlsession.selectList("board.boardList");
		return boardList;
	}


	// ===== #63. 글 1개 보여주기 =====
	@Override
	public BoardVO getView(String seq) {
		BoardVO boardvo = sqlsession.selectOne("board.getView", seq);
		return boardvo;
	}

	// ===== #64. 글 1개 보여줄때 조회수(readCount) 1증가 시키기 =====
	@Override
	public void setAddReadCount(String seq) {
		sqlsession.update("board.setAddReadCount", seq);
	}


	// ==== #73. 글수정 및 글삭제시 암호일치 여부 알아오기 ====
	@Override
	public boolean checkPw(HashMap<String,String> map) {
		int n = sqlsession.selectOne("board.checkPw", map);
		boolean result = false;
		
		if(n == 1) 
			result = true;
		
		return result;
	}


	// ==== #74. 글 1개 수정하기 ==== 
	@Override
	public int updateContent(BoardVO boardvo) {
		int n = sqlsession.update("board.updateContent", boardvo);
		return n;
	}


	// ==== #80. 글 1개 삭제하기 ====
	@Override
	public int deleteContent(HashMap<String, String> map) {
		int n = sqlsession.update("board.deleteContent", map);
		return n;
	}


	// ==== #87. 댓글쓰기 ==== 
	@Override
	public int addComment(CommentVO commentvo) {
		int n = sqlsession.insert("board.addComment", commentvo);
		return n;
	}


	// ==== #88. 댓글쓰기 이후에 댓글의 갯수(commentCount 컬럼) 1증가 시키기 
	@Override
	public int updateCommentCount(String parentSeq) {
		int n = sqlsession.update("board.updateCommentCount", parentSeq);
		return n;
	}


	// ==== #93. 댓글내용 보여주기 ==== 
	@Override
	public List<CommentVO> listComment(String seq) {
		List<CommentVO> list = sqlsession.selectList("board.listComment", seq);
		return list;
	}


	// ==== #99. 원게시글에 딸린 댓글이 있는지 없는지를 확인하기  ==== 
	@Override
	public boolean isExistsComment(HashMap<String, String> map) {
		int count = sqlsession.selectOne("board.isExistsComment", map);
		if(count > 0)
			return true;
		else
			return false;
	}


	// ==== #100. 원게시글에 딸린 댓글들 삭제하기 ==== 
	@Override
	public int deleteComment(HashMap<String, String> map) {
		int n = sqlsession.update("board.deleteComment", map);
		return n;
	}


	// ==== #108. 글목록 보여주기(검색어가 없는 것) ==== 
	@Override
	public List<BoardVO> boardList(HashMap<String, String> map) {
		List<BoardVO> boardList = sqlsession.selectList("board.boardList", map);
		return boardList;
	}

	// ==== 글목록 보여주기(검색어가 있는 것) ==== 
	@Override
	public List<BoardVO> boardList2(HashMap<String, String> map) {
		List<BoardVO> boardList = sqlsession.selectList("board.boardList2", map); 
		return boardList;
	}


	// ==== #115. 총게시물 건수 구하기
	// 검색어가 없는 총게시물 건수
	@Override
	public int getTotalCount() {
		int count = sqlsession.selectOne("board.getTotalCount");
		return count;
	}

	// 검색어가 있는 총게시물 건수
	@Override
	public int getTotalCount2(HashMap<String, String> map) {
		int count = sqlsession.selectOne("board.getTotalCount2", map);
		return count;
	}

	// ==== #124. tblBoard 테이블의 groupno 의 max값 알아오기 ====
	@Override
	public int getGroupMaxno() {
		int max = sqlsession.selectOne("board.getGroupMaxno");
		return max;
	}

	// ==== #140. 파일첨부가 있는 글쓰기
	@Override
	public int add_withFile(BoardVO boardvo) {
		int n = sqlsession.insert("board.add_withFile", boardvo);
		return n;
	}	
	
	
	
}
