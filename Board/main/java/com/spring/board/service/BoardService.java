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

// ===== #30. Service 선언 ======
@Service
public class BoardService implements InterBoardService {

	// ===== #31. 의존객체 주입하기(DI : Dependency Injection) =====
	@Autowired
	private InterBoardDAO dao;

	
	// ===== #41. 메인페이지용 이미지 파일이름을 가져오는 서비스단 List<String> getImgfilenameList() 메소드 생성하기 =====  
	@Override
	public List<String> getImgfilenameList() {
		List<String> list = dao.getImgfilenameList();
		return list;
	}


	// ===== #46. 로그인 여부 알아오기  =====
	@Override
	public MemberVO getLoginMemeber(HashMap<String, String> map) {
		MemberVO loginuser = dao.getLoginMemeber(map);
		return loginuser;
	}


	// ==== #54. 글쓰기(파일첨부가 없는 글쓰기) ====
	@Override
	public int add(BoardVO boardvo) {
		
		// ==== #123. 
		//		글쓰기가 원글쓰기인지 아니면 답변글쓰기인지를 구분하여 tblBoard 테이블에 insert를 해주어야 한다. 
		//		- 원글쓰기 라면 tblBoard 테이블의 groupno 컬럼의 값은 max값 +1 로 해야하고, 
		//		- 답변글쓰기 이라면 넘겨받은 값을 그대로 insert 해주어야 한다. ====
		
		// 원글쓰기인지, 답변글쓰기인지 구분하기
		if(boardvo.getFk_seq() == null || boardvo.getFk_seq().trim().isEmpty()) {	// null 이던지 텅비었다면 (.equals 도 괜찮다)
			// 원글 쓰기인 경우
			int groupno = dao.getGroupMaxno()+1;			// 만든다
			boardvo.setGroupno(String.valueOf(groupno));	// 넘어온 boardvo에서 위에 만든것으로 바꿔준다. VO에 String타입이기때문에
		}		
		
		int n = dao.add(boardvo);		
		
		return n;
	}


	// ===== #58. 글목록 보여주기(검색어가 없는 전체글목록 보여주기) =====
	@Override
	public List<BoardVO> boardList() {
		List<BoardVO> boardList = dao.boardList();
		return boardList;
	}


	// ==== #62. 조회수(readCount) 증가한 후에 글1개를 가져오는 것 ====
	//           단, 다른사람의 글일때만 조회수 1증가 시킨다.
	@Override
	public BoardVO getView(String seq, String userid) {
		
		BoardVO boardvo = dao.getView(seq);
				
		if(userid != null && !boardvo.getUserid().equals(userid)) {
			dao.setAddReadCount(seq);
			boardvo = dao.getView(seq);
		}
		
		return boardvo;
	}


	// ==== #69. 조회수 1증가 없이 그냥 글 1개를 가져오는 것 ====
	@Override
	public BoardVO getViewWithNoReadCount(String seq) {
		BoardVO boardvo = dao.getView(seq);
		return boardvo;
	}


	// ==== #72. 1개글 수정하기 ====
	@Override
	public int edit(BoardVO boardvo) {
		
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("seq", boardvo.getSeq());
		map.put("pw", boardvo.getPw());
		
		boolean checkPw = dao.checkPw(map);
		// 글번호에 대한 암호가 일치하면 true 반환.
		// 글번호에 대한 암호가 일치하지 않으면 false 반환.
		
		int n = 0;
		
		if(checkPw)
			n = dao.updateContent(boardvo); // 글 1개 수정하기
		
		return n;
	}


	// ==== #79. 1개글 삭제하기 ====
	@Override
//	public int del(HashMap<String, String> map) {
//  ==== #96. 트랜잭션 처리를 위해서 먼저 위의 줄을 주석처리를 하고 아래와 같이 한다.
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor={Throwable.class} ) 
	public int del(HashMap<String, String> map) throws Throwable {	 	
		
		boolean checkPw = dao.checkPw(map);
		// 글번호에 대한 암호가 일치하면 true 반환.
		// 글번호에 대한 암호가 일치하지 않으면 false 반환.
		
		int result1=0, result2=0, n=0;
		boolean bool = false;
		
		if(checkPw) {
			// ==== #97. 원게시글에 딸린 댓글이 있는지 없는지를 확인하기 ==== 
			bool = dao.isExistsComment(map);
			
			result1 = dao.deleteContent(map); // 글 1개 삭제하기
			
			if(bool) { // ==== #98. 원게시글에 딸린 댓글들 삭제하기
				result2 = dao.deleteComment(map);  
			}
		}
		
		if( (bool==true && result1==1 && result2>0) ||
			(bool==false && result1==1 && result2==0) )	
			n = 1;
		
		return n;
	}


	// ==== #86. 댓글쓰기 ====
    /*
       tblComment 테이블에 insert 된 다음에
       tblBoard 테이블에 commentCount 컬럼의 값이 1증가(update)하도록 요청한다.
             즉, 2개 이상의 DML 처리를 해야 하므로 Transaction 처리를 해야 한다.
             
       >>>> 트랜잭션처리를 해야할 메소드에 @Transactional 어노테이션을 설정하면 된다.
	   rollbackFor={Throwable.class} 은 롤백을 해야할 범위를 말하는데
	   Throwable.class 은 error 및 exception 을 포함한 최상위 루트이다.
	      즉, 해당 메소드 실행시 발생하는 모든 error 및 exception 에 대해서 롤백을 하겠다는 말이다.         	 
    */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor={Throwable.class} )  
	public int addComment(CommentVO commentvo) throws Throwable {
		
		int result = 0;
		
		int n = 0;
		
		n = dao.addComment(commentvo); 
		// tblComment 테이블에 insert
		
		if(n == 1) { // tblComment 테이블에 insert가 성공했다라면
		
			result = dao.updateCommentCount(commentvo.getParentSeq());
			// tblBoard 테이블에 commentCount 컬럼의 값이 1증가(update)
		}
		
		return result;
	}


	// ==== #92. 댓글내용 보여주기 ==== 
	@Override
	public List<CommentVO> listComment(String seq) {
		List<CommentVO> list = dao.listComment(seq);
		return list;
	}


	// ==== #107. 글목록 보여주기(검색어가 없는 것) ==== 
	@Override
	public List<BoardVO> boardList(HashMap<String, String> map) {
		
		List<BoardVO> boardList = dao.boardList(map);
		
		return boardList;
	}
	
	// ==== 글목록 보여주기(검색어가 있는 것) ==== 
	@Override
	public List<BoardVO> boardList2(HashMap<String, String> map) {
		
		List<BoardVO> boardList = dao.boardList2(map);
		
		return boardList;
	}


	// ==== #114. 총게시물 건수 구하기
	// 검색어가 없는 총게시물 건수
	@Override
	public int getTotalCount() {
		int count = dao.getTotalCount();
		return count;
	}

	// 검색어가 있는 총게시물 건수
	@Override
	public int getTotalCount2(HashMap<String, String> map) {
		int count = dao.getTotalCount2(map);
		return count;
	}

	// ==== #139. 파일첨부가 있는 글쓰기
	@Override
	public int add_withFile(BoardVO boardvo) {	 
		//	글쓰기가 원글쓰기인지 아니면 답변글쓰기인지를 구분하여 tblBoard 테이블에 insert를 해주어야 한다. 
		//	- 원글쓰기 라면 tblBoard 테이블의 groupno 컬럼의 값은 max값 +1 로 해야하고, 
		//	- 답변글쓰기 이라면 넘겨받은 값을 그대로 insert 해주어야 한다. ====
		
		// 원글쓰기인지, 답변글쓰기인지 구분하기
		if(boardvo.getFk_seq() == null || boardvo.getFk_seq().trim().isEmpty()) {	// null 이던지 텅비었다면 (.equals 도 괜찮다)
			// 원글 쓰기인 경우
			int groupno = dao.getGroupMaxno()+1;			// 만든다
			boardvo.setGroupno(String.valueOf(groupno));	// 넘어온 boardvo에서 위에 만든것으로 바꿔준다. VO에 String타입이기때문에
		}		
		
		int n = dao.add_withFile(boardvo);		
		
		return n;
	}
	
}
