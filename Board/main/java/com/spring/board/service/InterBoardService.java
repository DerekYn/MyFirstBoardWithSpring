package com.spring.board.service;

import java.util.HashMap;
import java.util.List;

import com.spring.board.model.BoardVO;
import com.spring.board.model.CommentVO;
import com.spring.member.model.MemberVO;

// === Service�� �������̽� ���� ===
public interface InterBoardService {

	List<String> getImgfilenameList(); 								// �̹��� ���ϸ� ��������

	MemberVO getLoginMemeber(HashMap<String, String> map); 			// �α��� ���� �˾ƿ���   

	int add(BoardVO boardvo); 										// �۾���(����÷�ΰ� ���� �۾���)

	List<BoardVO> boardList(); 										// �۸�� �����ֱ�(�˻�� ���� ��ü�۸�� �����ֱ�) 

	BoardVO getView(String seq, String userid); 					// ��ȸ��(readCount) ������ �Ŀ� ��1���� �������� ��.
	// BoardVO�� ����Ÿ������ �� 1���� �������°�
	
	// ��, �ڽ��� �� ���� �ڽ��� �����ÿ��� ��ȸ���� �������� �ʰ�,
	// �ٸ� ����� �� ���̶�� ��ȸ���� �����ǵ��� �ؾ� �Ѵ�.
	// �α��� ���� ���� ���¿��� ���� ������ ��ȸ�� ������ �Ͼ�� �ʵ��� �Ѵ�. 

	BoardVO getViewWithNoReadCount(String seq); 					// ��ȸ�� 1���� ���� �׳� �� 1���� �������� ��

	int edit(BoardVO boardvo); 										// 1���� �����ϱ� 

//	int del(HashMap<String, String> map); 							// 1���� �����ϱ�
	int del(HashMap<String, String> map) throws Throwable; 			// 1���� �����ϱ�

	int addComment(CommentVO commentvo) throws Throwable; 			// ��۾��� 
	
	List<CommentVO> listComment(String seq); 						// ��۳��� �����ֱ�

	List<BoardVO> boardList(HashMap<String, String> map);  			// �۸�� �����ֱ�(�˻�� ���� ��)
	List<BoardVO> boardList2(HashMap<String, String> map); 			// �۸�� �����ֱ�(�˻�� �ִ� ��)   

	int getTotalCount();  											// �˻�� ���� �ѰԽù� �Ǽ�
	int getTotalCount2(HashMap<String, String> map); 				// �˻�� �ִ� �ѰԽù� �Ǽ�

	int add_withFile(BoardVO boardvo);								// ���� ÷�ΰ� �ִ� �۾���
	
	      
	
	
	
	
	

}
