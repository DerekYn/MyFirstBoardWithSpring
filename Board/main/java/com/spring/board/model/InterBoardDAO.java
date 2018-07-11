package com.spring.board.model;

import java.util.HashMap;
import java.util.List;

import com.spring.member.model.MemberVO;

// ==== model��(DAO)�� �������̽� ���� ====
public interface InterBoardDAO {

	List<String> getImgfilenameList(); 							// �̹��� ���ϸ� ��������

	MemberVO getLoginMemeber(HashMap<String, String> map); 		// �α��� ���� �˾ƿ��� 

	int add(BoardVO boardvo); 									// �۾���(����÷�ΰ� ���� �۾���)

	List<BoardVO> boardList(); 									// �۸�� �����ֱ�(�˻�� ���� ��ü�۸�� �����ֱ�)

	BoardVO getView(String seq);								// �� 1���� �����ֱ�

	void setAddReadCount(String seq); 							// �� 1���� ���� ��ȸ��(readCount) 1���� ��Ű�� 

	boolean checkPw(HashMap<String,String> map); 				// �ۼ��� �� �ۻ����� ��ȣ��ġ ���� �˾ƿ���

	int updateContent(BoardVO boardvo); 						// �� 1�� �����ϱ�

	int deleteContent(HashMap<String, String> map); 			// �� 1�� �����ϱ�

	int addComment(CommentVO commentvo); 						// ��۾���
	int updateCommentCount(String parentSeq); 					// ��۾��� ���Ŀ� ����� ����(commentCount �÷�) 1���� ��Ű��  

	List<CommentVO> listComment(String seq); 					// ��۳��� �����ֱ�

	boolean isExistsComment(HashMap<String, String> map); 		// ���Խñۿ� ���� ����� �ִ��� �������� Ȯ���ϱ�  
	int deleteComment(HashMap<String, String> map); 			// ���Խñۿ� ���� ��۵� �����ϱ�

	List<BoardVO> boardList(HashMap<String, String> map); 		// �۸�� �����ֱ�(�˻�� ���� ��)
	List<BoardVO> boardList2(HashMap<String, String> map);		// �۸�� �����ֱ�(�˻�� �ִ� ��)

	int getTotalCount(); 										// �˻�� ���� �ѰԽù� �Ǽ�
	int getTotalCount2(HashMap<String, String> map); 			// �˻�� �ִ� �ѰԽù� �Ǽ�

	int getGroupMaxno();										// tblBoard ���̺��� groupno �� max�� �˾ƿ���

	int add_withFile(BoardVO boardvo);							// ����÷�ΰ� �ִ� �۾���
	
	
	
	

}
