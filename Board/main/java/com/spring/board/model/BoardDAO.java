package com.spring.board.model;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.member.model.MemberVO;

// ===== #28. DAO ���� =====
@Repository
public class BoardDAO implements InterBoardDAO {

	// ===== #29. ������ü �����ϱ�(DI : Dependency Injection) =====
	@Autowired
	private SqlSessionTemplate sqlsession;
	
	
	// ==== #42. ������������ �̹��� �����̸��� �������� �޼ҵ� �����ϱ� ====
	@Override
	public List<String> getImgfilenameList() {
		List<String> list = sqlsession.selectList("board.getImgfilenameList");
		return list;
	}


	// ===== #47. �α��� ���� �˾ƿ��� =====
	@Override
	public MemberVO getLoginMemeber(HashMap<String, String> map) {
		MemberVO loginuser = sqlsession.selectOne("board.getLoginMemeber", map);
		return loginuser;
	}


	// ==== #55. �۾���(����÷�ΰ� ���� �۾���) ====
	@Override
	public int add(BoardVO boardvo) {
		int n = sqlsession.insert("board.add", boardvo);
		return n;
	}

    // ==== #59. �۸�� �����ֱ�(�˻�� ���� ��ü�۸�� �����ֱ�) ====
	@Override
	public List<BoardVO> boardList() {
		List<BoardVO> boardList = sqlsession.selectList("board.boardList");
		return boardList;
	}


	// ===== #63. �� 1�� �����ֱ� =====
	@Override
	public BoardVO getView(String seq) {
		BoardVO boardvo = sqlsession.selectOne("board.getView", seq);
		return boardvo;
	}

	// ===== #64. �� 1�� �����ٶ� ��ȸ��(readCount) 1���� ��Ű�� =====
	@Override
	public void setAddReadCount(String seq) {
		sqlsession.update("board.setAddReadCount", seq);
	}


	// ==== #73. �ۼ��� �� �ۻ����� ��ȣ��ġ ���� �˾ƿ��� ====
	@Override
	public boolean checkPw(HashMap<String,String> map) {
		int n = sqlsession.selectOne("board.checkPw", map);
		boolean result = false;
		
		if(n == 1) 
			result = true;
		
		return result;
	}


	// ==== #74. �� 1�� �����ϱ� ==== 
	@Override
	public int updateContent(BoardVO boardvo) {
		int n = sqlsession.update("board.updateContent", boardvo);
		return n;
	}


	// ==== #80. �� 1�� �����ϱ� ====
	@Override
	public int deleteContent(HashMap<String, String> map) {
		int n = sqlsession.update("board.deleteContent", map);
		return n;
	}


	// ==== #87. ��۾��� ==== 
	@Override
	public int addComment(CommentVO commentvo) {
		int n = sqlsession.insert("board.addComment", commentvo);
		return n;
	}


	// ==== #88. ��۾��� ���Ŀ� ����� ����(commentCount �÷�) 1���� ��Ű�� 
	@Override
	public int updateCommentCount(String parentSeq) {
		int n = sqlsession.update("board.updateCommentCount", parentSeq);
		return n;
	}


	// ==== #93. ��۳��� �����ֱ� ==== 
	@Override
	public List<CommentVO> listComment(String seq) {
		List<CommentVO> list = sqlsession.selectList("board.listComment", seq);
		return list;
	}


	// ==== #99. ���Խñۿ� ���� ����� �ִ��� �������� Ȯ���ϱ�  ==== 
	@Override
	public boolean isExistsComment(HashMap<String, String> map) {
		int count = sqlsession.selectOne("board.isExistsComment", map);
		if(count > 0)
			return true;
		else
			return false;
	}


	// ==== #100. ���Խñۿ� ���� ��۵� �����ϱ� ==== 
	@Override
	public int deleteComment(HashMap<String, String> map) {
		int n = sqlsession.update("board.deleteComment", map);
		return n;
	}


	// ==== #108. �۸�� �����ֱ�(�˻�� ���� ��) ==== 
	@Override
	public List<BoardVO> boardList(HashMap<String, String> map) {
		List<BoardVO> boardList = sqlsession.selectList("board.boardList", map);
		return boardList;
	}

	// ==== �۸�� �����ֱ�(�˻�� �ִ� ��) ==== 
	@Override
	public List<BoardVO> boardList2(HashMap<String, String> map) {
		List<BoardVO> boardList = sqlsession.selectList("board.boardList2", map); 
		return boardList;
	}


	// ==== #115. �ѰԽù� �Ǽ� ���ϱ�
	// �˻�� ���� �ѰԽù� �Ǽ�
	@Override
	public int getTotalCount() {
		int count = sqlsession.selectOne("board.getTotalCount");
		return count;
	}

	// �˻�� �ִ� �ѰԽù� �Ǽ�
	@Override
	public int getTotalCount2(HashMap<String, String> map) {
		int count = sqlsession.selectOne("board.getTotalCount2", map);
		return count;
	}

	// ==== #124. tblBoard ���̺��� groupno �� max�� �˾ƿ��� ====
	@Override
	public int getGroupMaxno() {
		int max = sqlsession.selectOne("board.getGroupMaxno");
		return max;
	}

	// ==== #140. ����÷�ΰ� �ִ� �۾���
	@Override
	public int add_withFile(BoardVO boardvo) {
		int n = sqlsession.insert("board.add_withFile", boardvo);
		return n;
	}	
	
	
	
}
