package com.spring.board.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.board.model.BoardVO;
import com.spring.board.model.CommentVO;
import com.spring.board.model.InterBoardDAO;
import com.spring.member.model.MemberVO;

// ===== #30. Service ���� ======
@Service
public class BoardService implements InterBoardService {

	// ===== #31. ������ü �����ϱ�(DI : Dependency Injection) =====
	@Autowired
	private InterBoardDAO dao;

	
	// ===== #41. ������������ �̹��� �����̸��� �������� ���񽺴� List<String> getImgfilenameList() �޼ҵ� �����ϱ� =====  
	@Override
	public List<String> getImgfilenameList() {
		List<String> list = dao.getImgfilenameList();
		return list;
	}


	// ===== #46. �α��� ���� �˾ƿ���  =====
	@Override
	public MemberVO getLoginMemeber(HashMap<String, String> map) {
		MemberVO loginuser = dao.getLoginMemeber(map);
		return loginuser;
	}


	// ==== #54. �۾���(����÷�ΰ� ���� �۾���) ====
	@Override
	public int add(BoardVO boardvo) {
		
		// ==== #123. 
		//		�۾��Ⱑ ���۾������� �ƴϸ� �亯�۾��������� �����Ͽ� tblBoard ���̺� insert�� ���־�� �Ѵ�. 
		//		- ���۾��� ��� tblBoard ���̺��� groupno �÷��� ���� max�� +1 �� �ؾ��ϰ�, 
		//		- �亯�۾��� �̶�� �Ѱܹ��� ���� �״�� insert ���־�� �Ѵ�. ====
		
		// ���۾�������, �亯�۾������� �����ϱ�
		if(boardvo.getFk_seq() == null || boardvo.getFk_seq().trim().isEmpty()) {	// null �̴��� �ֺ���ٸ� (.equals �� ������)
			// ���� ������ ���
			int groupno = dao.getGroupMaxno()+1;			// �����
			boardvo.setGroupno(String.valueOf(groupno));	// �Ѿ�� boardvo���� ���� ��������� �ٲ��ش�. VO�� StringŸ���̱⶧����
		}		
		
		int n = dao.add(boardvo);		
		
		return n;
	}


	// ===== #58. �۸�� �����ֱ�(�˻�� ���� ��ü�۸�� �����ֱ�) =====
	@Override
	public List<BoardVO> boardList() {
		List<BoardVO> boardList = dao.boardList();
		return boardList;
	}


	// ==== #62. ��ȸ��(readCount) ������ �Ŀ� ��1���� �������� �� ====
	//           ��, �ٸ������ ���϶��� ��ȸ�� 1���� ��Ų��.
	@Override
	public BoardVO getView(String seq, String userid) {
		
		BoardVO boardvo = dao.getView(seq);
				
		if(userid != null && !boardvo.getUserid().equals(userid)) {
			dao.setAddReadCount(seq);
			boardvo = dao.getView(seq);
		}
		
		return boardvo;
	}


	// ==== #69. ��ȸ�� 1���� ���� �׳� �� 1���� �������� �� ====
	@Override
	public BoardVO getViewWithNoReadCount(String seq) {
		BoardVO boardvo = dao.getView(seq);
		return boardvo;
	}


	// ==== #72. 1���� �����ϱ� ====
	@Override
	public int edit(BoardVO boardvo) {
		
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("seq", boardvo.getSeq());
		map.put("pw", boardvo.getPw());
		
		boolean checkPw = dao.checkPw(map);
		// �۹�ȣ�� ���� ��ȣ�� ��ġ�ϸ� true ��ȯ.
		// �۹�ȣ�� ���� ��ȣ�� ��ġ���� ������ false ��ȯ.
		
		int n = 0;
		
		if(checkPw)
			n = dao.updateContent(boardvo); // �� 1�� �����ϱ�
		
		return n;
	}


	// ==== #79. 1���� �����ϱ� ====
	@Override
//	public int del(HashMap<String, String> map) {
//  ==== #96. Ʈ����� ó���� ���ؼ� ���� ���� ���� �ּ�ó���� �ϰ� �Ʒ��� ���� �Ѵ�.
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor={Throwable.class} ) 
	public int del(HashMap<String, String> map) throws Throwable {	 	
		
		boolean checkPw = dao.checkPw(map);
		// �۹�ȣ�� ���� ��ȣ�� ��ġ�ϸ� true ��ȯ.
		// �۹�ȣ�� ���� ��ȣ�� ��ġ���� ������ false ��ȯ.
		
		int result1=0, result2=0, n=0;
		boolean bool = false;
		
		if(checkPw) {
			// ==== #97. ���Խñۿ� ���� ����� �ִ��� �������� Ȯ���ϱ� ==== 
			bool = dao.isExistsComment(map);
			
			result1 = dao.deleteContent(map); // �� 1�� �����ϱ�
			
			if(bool) { // ==== #98. ���Խñۿ� ���� ��۵� �����ϱ�
				result2 = dao.deleteComment(map);  
			}
		}
		
		if( (bool==true && result1==1 && result2>0) ||
			(bool==false && result1==1 && result2==0) )	
			n = 1;
		
		return n;
	}


	// ==== #86. ��۾��� ====
    /*
       tblComment ���̺� insert �� ������
       tblBoard ���̺� commentCount �÷��� ���� 1����(update)�ϵ��� ��û�Ѵ�.
             ��, 2�� �̻��� DML ó���� �ؾ� �ϹǷ� Transaction ó���� �ؾ� �Ѵ�.
             
       >>>> Ʈ�����ó���� �ؾ��� �޼ҵ忡 @Transactional ������̼��� �����ϸ� �ȴ�.
	   rollbackFor={Throwable.class} �� �ѹ��� �ؾ��� ������ ���ϴµ�
	   Throwable.class �� error �� exception �� ������ �ֻ��� ��Ʈ�̴�.
	      ��, �ش� �޼ҵ� ����� �߻��ϴ� ��� error �� exception �� ���ؼ� �ѹ��� �ϰڴٴ� ���̴�.         	 
    */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor={Throwable.class} )  
	public int addComment(CommentVO commentvo) throws Throwable {
		
		int result = 0;
		
		int n = 0;
		
		n = dao.addComment(commentvo); 
		// tblComment ���̺� insert
		
		if(n == 1) { // tblComment ���̺� insert�� �����ߴٶ��
		
			result = dao.updateCommentCount(commentvo.getParentSeq());
			// tblBoard ���̺� commentCount �÷��� ���� 1����(update)
		}
		
		return result;
	}


	// ==== #92. ��۳��� �����ֱ� ==== 
	@Override
	public List<CommentVO> listComment(String seq) {
		List<CommentVO> list = dao.listComment(seq);
		return list;
	}


	// ==== #107. �۸�� �����ֱ�(�˻�� ���� ��) ==== 
	@Override
	public List<BoardVO> boardList(HashMap<String, String> map) {
		
		List<BoardVO> boardList = dao.boardList(map);
		
		return boardList;
	}
	
	// ==== �۸�� �����ֱ�(�˻�� �ִ� ��) ==== 
	@Override
	public List<BoardVO> boardList2(HashMap<String, String> map) {
		
		List<BoardVO> boardList = dao.boardList2(map);
		
		return boardList;
	}


	// ==== #114. �ѰԽù� �Ǽ� ���ϱ�
	// �˻�� ���� �ѰԽù� �Ǽ�
	@Override
	public int getTotalCount() {
		int count = dao.getTotalCount();
		return count;
	}

	// �˻�� �ִ� �ѰԽù� �Ǽ�
	@Override
	public int getTotalCount2(HashMap<String, String> map) {
		int count = dao.getTotalCount2(map);
		return count;
	}

	// ==== #139. ����÷�ΰ� �ִ� �۾���
	@Override
	public int add_withFile(BoardVO boardvo) {	 
		//	�۾��Ⱑ ���۾������� �ƴϸ� �亯�۾��������� �����Ͽ� tblBoard ���̺� insert�� ���־�� �Ѵ�. 
		//	- ���۾��� ��� tblBoard ���̺��� groupno �÷��� ���� max�� +1 �� �ؾ��ϰ�, 
		//	- �亯�۾��� �̶�� �Ѱܹ��� ���� �״�� insert ���־�� �Ѵ�. ====
		
		// ���۾�������, �亯�۾������� �����ϱ�
		if(boardvo.getFk_seq() == null || boardvo.getFk_seq().trim().isEmpty()) {	// null �̴��� �ֺ���ٸ� (.equals �� ������)
			// ���� ������ ���
			int groupno = dao.getGroupMaxno()+1;			// �����
			boardvo.setGroupno(String.valueOf(groupno));	// �Ѿ�� boardvo���� ���� ��������� �ٲ��ش�. VO�� StringŸ���̱⶧����
		}		
		
		int n = dao.add_withFile(boardvo);		
		
		return n;
	}
	
}
