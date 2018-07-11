package com.spring.board;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.spring.board.model.BoardVO;
import com.spring.board.model.CommentVO;
import com.spring.board.model.PhotoVO;
import com.spring.board.service.InterBoardService;
import com.spring.common.FileManager;
import com.spring.common.MyUtil;
import com.spring.member.model.MemberVO;

// ===== #32. 컨트롤러 선언 =====
@Controller
@Component
/*
 * XML에서 빈을 만드는 대신에 클래스명 앞에 @Component 어노테이션을 적어주면 해당 클래스(지금은 BoardController)는
 * bean으로 자동 등록된다. 그리고 bean의 이름(첫글자는 소문자)은 해당 클래스명(지금은 BoardController)이 된다. 지금은
 * bean의 이름은 boardController 이 된다.
 */
public class BoardController {

	// ===== #33. 의존객체 주입하기(DI : Dependency Injection) =====
	@Autowired
	private InterBoardService service;

	// ===== #132. 파일업로드 및 다운로드를 해주는 FileManager 클래스 의존객체 주입하기(DI : Dependency
	// Injection) =====
	@Autowired
	private FileManager filemanager;

	// ==== #40. 메인 페이지 요청 ====
	@RequestMapping(value = "/index.action", method = { RequestMethod.GET })
	public String index(HttpServletRequest req) {

		List<String> imgfilenameList = service.getImgfilenameList();

		req.setAttribute("imgfilenameList", imgfilenameList);

		return "main/index.tiles";
		// /Board/src/main/webapp/WEB-INF/views/main/index.jsp 파일을 생성한다.
	}

	// ==== #44. 로그인 페이지 요청 ====
	@RequestMapping(value = "/login.action", method = { RequestMethod.GET })
	public String login(HttpServletRequest req) {

		return "login/loginform.tiles";
		// /Board/src/main/webapp/WEB-INF/views/login/loginform.jsp 파일을 생성한다.
	}

	// ==== #45. 로그인 여부 요청 ====
	@RequestMapping(value = "/loginEnd.action", method = { RequestMethod.POST })
	public String loginEnd(HttpServletRequest req) {

		String userid = req.getParameter("userid");
		String pwd = req.getParameter("pwd");

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("pwd", pwd);

		MemberVO loginuser = service.getLoginMemeber(map);

		if (loginuser != null) {
			HttpSession session = req.getSession();
			session.setAttribute("loginuser", loginuser);

			String gobackURL = (String) session.getAttribute("gobackURL");
			req.setAttribute("gobackURL", gobackURL);
			session.removeAttribute("gobackURL");
		}

		return "login/loginEnd.tiles";
		// /Board/src/main/webapp/WEB-INF/views/login/loginEnd.jsp 파일을 생성한다.
	}

	// ==== #50. 로그아웃 완료 요청 ====
	@RequestMapping(value = "/logout.action", method = { RequestMethod.GET })
	public String logout(HttpServletRequest req) {

		HttpSession session = req.getSession();
		session.invalidate();

		return "login/logout.tiles";
		// /Board/src/main/webapp/WEB-INF/views/login/logout.jsp 파일을 생성한다.
	}

	// ===== #51. 글쓰기 form 페이지 요청 =====
	@RequestMapping(value = "/add.action", method = { RequestMethod.GET })
	public String requireLogin_add(HttpServletRequest req, HttpServletResponse res) {

		// ==== #121. 답변글쓰기 추가 되었으므로 아래와 같이 한다. (시작) ====
		String fk_seq = req.getParameter("fk_seq"); // view.jsp에서 넘겨준 것을 받아오자
		String groupno = req.getParameter("groupno");
		String depthno = req.getParameter("depthno");

		req.setAttribute("fk_seq", fk_seq);
		req.setAttribute("groupno", groupno);
		req.setAttribute("depthno", depthno);

		// ==== 답변글쓰기 추가 되었으므로 아래와 같이 한다. (끝) ====

		return "board/add.tiles2";
		// /Board/src/main/webapp/WEB-INF/views2/board/add.jsp 파일을 생성한다.
	}

	// ===== #53-2. 글쓰기 완료 요청 =====
	@RequestMapping(value = "/addEnd.action", method = { RequestMethod.POST })
	// public String addEnd(BoardVO boardvo, HttpServletRequest req) { // boardvo 를
	// 깠을 때 fk_seq 가 null 이라면 원글쓰기이다.
	/*
	 * ==== #136. 파일첨부가 된 글쓰기 ==== 먼저, 위의 ublic String addEnd(BoardVO boardvo,
	 * HttpServletRequest req) { 을 주석처리를 한 후 아래와 같이 한다.
	 */
	public String addEnd(BoardVO boardvo, MultipartHttpServletRequest req, HttpSession session) { // 그냥 Request는 파일첨부
																									// 불가.
																									// MultipartHttpServletRequest
																									// 를 사용해야 한다.

		// ==== #137. 사용자가 쓴 글에 파일이 첨부가 된 것인지 아니면 되지 않은 것인지 구분을 지어야 한다. ====

		//////////////// === 첨부파일이 있으면 파일업로드 하기 시작 === ////////////////
		if (!boardvo.getAttach().isEmpty()) {
			// attach가 비어있지 않으면(즉, 첨부파일이 있는 경우라면)

			/*
			 * 1. 사용자가 보낸 파일을 WAS(톰캣)의 특정 경로 폴더에 저장해주어야 한다. >>>> 파일이 업로드 되어질 특정 경로(폴더)지정해주기
			 * 우리는 WAS(톰캣)의 webapp/resources/files 라는 폴더로 지정해주겠다.
			 */

			// WAS(톰캣)의 webapp 의 절대경로를 알아와야 한다.
			String root = session.getServletContext().getRealPath("/");
			String path = root + "resources" + File.separator + "files";
			// File.separator ==> 운영체제(OS)가 Windows 라면 "\" 이고,
			// 운영체제가 UNIX, Linux 라면 "/" 이다.

			System.out.println("확인용 path : " + path);
			// 확인용 path :
			// C:\springworkspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Board\resources\files

			// 2. 파일첨부를 위한 변수의 설정 및 값을 초기화한 후 파일올리기
			String newFileName = ""; // WAS(톰캣) 디스크에 저장할 파일명.

			byte[] bytes = null; // 첨부파일을 WAS(톰캣) 디스크에 저장할 때 사용되는 용도.

			long fileSize = 0; // 파일크기를 읽어오기 위한 용도

			try { // FileManager.java에서 throws Exception 되어있다. 그래서 하는거임
				bytes = boardvo.getAttach().getBytes();
				// getBytes() ==> 첨부된 파일을 바이트단위로 파일을 다 읽어오는 것

				newFileName = filemanager.doFileUpload(bytes, boardvo.getAttach().getOriginalFilename(), path);
				// 파일을 업로드 한 후 현재시간+나노타임.확장자로 되어진 파일명을
				// 리턴받아 newFileName 으로 저장한다.
				// boardvo.getAttach().getOriginalFilename() 은 첨부된 파일의 실제 파일명(문자열)을 얻어오는 것이다.

				boardvo.setFileName(newFileName);
				// newFileName 이 WAS(톰캣)에 저장될 파일명(ex.201806283242343422424585453454547341.png)

				boardvo.setOrgFilename(boardvo.getAttach().getOriginalFilename());
				// boardvo.getAttach().getOriginalFilename() 은 진짜 파일명(ex. 강아지.png)
				// 사용자가 파일을 다운로드할 때 사용되어지는 파일명

				fileSize = boardvo.getAttach().getSize();
				// boardvo.getAttach().getSize() 은 첨부한 파일의 크기
				// 타입은 long 이다.

				boardvo.setFileSize(String.valueOf(fileSize)); // String 타입으로 변경 후 vo에 넣어준다.

			} catch (Exception e) {

			}

		}

		//////////////// === 첨부파일이 있으면 파일업로드 하기 끝 === /////////////////

		String content = boardvo.getContent().replaceAll("\r\n", "<br/>");
		boardvo.setContent(content);

		// int n = service.add(boardvo);
		// ==== #138. 파일첨부가 있는 글쓰기 이므로
		// 먼저 위의 메소드를 주석처리 하고서 아래와 같이한다.

		int n = 0;

		if (boardvo.getAttach().isEmpty()) { // 텅비었냐
			// 파일첨부가 없다면
			n = service.add(boardvo);
		}

		if (!boardvo.getAttach().isEmpty()) {
			// 파일첨부가 있다면
			n = service.add_withFile(boardvo);
		}

		req.setAttribute("n", n);

		return "board/addEnd.tiles2";
		// /Board/src/main/webapp/WEB-INF/views2/board/addEnd.jsp 파일을 생성한다.
	}

	// ===== #57. 글목록 보기 페이지 요청 =====
	@RequestMapping(value = "/list.action", method = { RequestMethod.GET })
	public String list(HttpServletRequest req) {

		// ==== #68. 글조회수(readCount)증가(DML문 update)는
		// 반드시 해당 글제목을 클릭했을 경우에만 증가되고
		// 웹브라우저에서 새로고침(F5)을 했을 경우에는 증가가 안되도록 한다.
		// 이것을 하기위한 표식을 단다. === 시작 ===
		HttpSession session = req.getSession();
		session.setAttribute("readCountPermission", "yes");
		// 반드시 웹브라우저 주소창에 /list.action 이라고 입력해야만
		// 세션에 "readCountPermission" 값이 저장되어있다.
		//// === 끝 ===

		List<BoardVO> boardList = null;

		// boardList = service.boardList(); // 검색어가 없는 경우 전체 조회해주는 것

		// ==== #106. 검색어가 포함되었으므로
		// 먼저 위의 boardList = service.boardList(); 부분을
		// 주석처리하고서 아래의 작업을 한다. ====
		String colname = req.getParameter("colname");
		String search = req.getParameter("search");

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("colname", colname);
		map.put("search", search);

		/*
		 * // === 페이징 처리 안 한것 === // if( (colname != null && search != null) &&
		 * (!colname.equals("null") && !search.equals("null")) &&
		 * (!colname.trim().isEmpty() && !search.trim().isEmpty()) ) { boardList =
		 * service.boardList2(map); // 검색어가 있는 것 } else { boardList =
		 * service.boardList(map); // 검색어가 없는 것 }
		 * 
		 * req.setAttribute("boardList", boardList); req.setAttribute("colname",
		 * colname); req.setAttribute("search", search);
		 */

		// ===== #110. 페이징 처리 하기 =====
		String str_currentShowPageNo = req.getParameter("currentShowPageNo");

		int totalCount = 0; // 총게시물건수
		int sizePerPage = 5; // 한 페이지당 보여줄 게시물 건수
		int currentShowPageNo = 0; // 현재 보여주는 페이지 번호로서, 초기치로는 1페이지로 설정함.
		int totalPage = 0; // 총페이지수 (웹브라우저상에 보여줄 총 페이지 갯수)

		int startRno = 0; // 시작행 번호
		int endRno = 0; // 끝행 번호

		int blockSize = 10; // "페이지바" 에 보여줄 페이지의 갯수

		/*
		 * ==== 총페이지수 구하기 ==== 검색조건이 없을 때의 총페이지 수와 검색조건이 있을 때의 총페이지 수를 구해야 한다.
		 * 
		 * 검색조건이 없을 때의 총페이지 수 ==> colname 과 search 값이 null 인 것이고, 검색조건이 있을 때의 총페이지 수 ==>
		 * colname 과 search 값이 null 이 아닌 것이다.
		 */
		// 먼저 총게시물 건수를 구한다.
		if ((colname != null && search != null) && (!colname.equals("null") && !search.equals("null"))
				&& (!colname.trim().isEmpty() && !search.trim().isEmpty())) {
			totalCount = service.getTotalCount2(map); // 검색어가 있는 총게시물 건수
		} else {
			totalCount = service.getTotalCount(); // 검색어가 없는 총게시물 건수
		}

		totalPage = (int) Math.ceil((double) totalCount / sizePerPage);

		if (str_currentShowPageNo == null) {
			// 게시판 초기화면에 보여지는 것은
			// req.getParameter("currentShowPageNo"); 값이 없으므로
			// str_currentShowPageNo 는 null 이 된다.

			currentShowPageNo = 1;
		} else {
			try {
				currentShowPageNo = Integer.parseInt(str_currentShowPageNo);

				if (currentShowPageNo < 1 || currentShowPageNo > totalPage) {
					currentShowPageNo = 1;
				}

			} catch (NumberFormatException e) {
				currentShowPageNo = 1;
			}
		}

		// **** 가져올 게시글의 범위를 구한다.(공식임!!!) ****
		/*
		 * currentShowPageNo startRno endRno --------------------------------------- 1
		 * page ==> 1 5 2 page ==> 6 10 3 page ==> 11 15 4 page ==> 16 20 5 page ==> 21
		 * 25 6 page ==> 26 30 7 page ==> 31 35
		 */

		startRno = (currentShowPageNo - 1) * sizePerPage + 1;
		endRno = startRno + sizePerPage - 1;

		// ==== #111. 페이징 처리를 위해 startRno, endRno 를 map 에 추가하여
		// 파라미터로 넘겨서 select 되도록 한다.
		map.put("startRno", String.valueOf(startRno));
		map.put("endRno", String.valueOf(endRno));

		if ((colname != null && search != null) && (!colname.equals("null") && !search.equals("null"))
				&& (!colname.trim().isEmpty() && !search.trim().isEmpty())) {
			boardList = service.boardList2(map); // 검색어가 있는 페이징처리
		} else {
			boardList = service.boardList(map); // 검색어가 없는 페이징처리
		}

		// ==== #113. 페이지바 만들기
		// (먼저, 페이지바에 나타낼 총페이지 갯수 구해야 하는데 우리는 위에서 구했다.) =====

		String pagebar = "<ul>";
		pagebar += MyUtil.getSearchPageBar("list.action", currentShowPageNo, sizePerPage, totalPage, blockSize, colname,
				search, null);
		pagebar += "</ul>";

		req.setAttribute("pagebar", pagebar);

		req.setAttribute("boardList", boardList);
		req.setAttribute("colname", colname);
		req.setAttribute("search", search);

		String goBackURL = MyUtil.getCurrentURL(req);
		req.setAttribute("goBackURL", goBackURL);

		return "board/list.tiles2";
		// /Board/src/main/webapp/WEB-INF/views2/board/list.jsp 파일을 생성한다.
	}

	// ==== #61. 글1개를 보여주는 페이지 요청하기 ====
	@RequestMapping(value = "/view.action", method = { RequestMethod.GET })
	public String view(HttpServletRequest req) {

		String seq = req.getParameter("seq"); // 글번호 받아오기
		String goBackURL = req.getParameter("goBackURL"); // 이전주소 가져오기

		BoardVO boardvo = null;

		// ==== #67. 글조회수(readCount)증가(DML문 update)는
		// 반드시 해당 글제목을 클릭했을 경우에만 증가되고
		// 웹브라우저에서 새로고침(F5)을 했을 경우에는 증가가 안되도록 한다.
		HttpSession session = req.getSession();

		if ("yes".equals(session.getAttribute("readCountPermission"))) {

			MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
			String userid = null;

			if (loginuser != null) {
				userid = loginuser.getUserid();
			}

			boardvo = service.getView(seq, userid);
			// 조회수(readCount) 증가한 후에 글1개를 가져오는 것.
			// 단, 자신이 쓴 글을 자신이 읽을시에는 조회수가 증가되지 않고,
			// 다른 사람이 쓴 글이라야 조회수가 증가되도록 해야 한다.
			// 로그인 하지 않은 상태에서 글을 읽을때 조회수 증가가 일어나지 않도록 한다.

			session.removeAttribute("readCountPermission");
		} else { // 웹브라우저에서 새로고침(F5)을 누른 경우

			// 조회수 1증가 없이 그냥 글 1개를 가져오는 것
			boardvo = service.getViewWithNoReadCount(seq);
		}

		req.setAttribute("boardvo", boardvo);

		// ==== #91. 댓글 내용 가져오기 ====
		List<CommentVO> commentList = service.listComment(seq);
		req.setAttribute("commentList", commentList);
		req.setAttribute("goBackURL", goBackURL);

		return "board/view.tiles2";
		// /Board/src/main/webapp/WEB-INF/views2/board/view.jsp 파일을 생성한다.
	}

	// ==== #70. 글수정 페이지 요청 ====
	@RequestMapping(value = "/edit.action", method = { RequestMethod.GET })
	public String requireLogin_edit(HttpServletRequest req, HttpServletResponse response) {

		String seq = req.getParameter("seq"); // 수정해야할 글번호 가져오기

		// 수정해야할 글 전체 내용 가져오기
		// 조회수 1증가 없이 그냥 글 1개를 가져오는 것
		BoardVO boardvo = service.getViewWithNoReadCount(seq);

		HttpSession session = req.getSession();
		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");

		if (!boardvo.getUserid().equals(loginuser.getUserid())) {
			String msg = "다른 사용자의 글은 수정이 불가합니다.";
			String loc = "javascript:history.back()";

			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);

			return "msg.notiles";
			// /Board/src/main/webapp/WEB-INF/viewsnotiles/msg.jsp 파일을 생성한다.
		} else {
			// 가져온 1개글을 request 영역에 저장시켜서 view 단 페이지로 넘긴다.
			req.setAttribute("boardvo", boardvo);

			return "board/edit.tiles2";
			// /Board/src/main/webapp/WEB-INF/views2/board/edit.jsp 파일을 생성한다.
		}

	}

	// ==== #71. 글수정 페이지 완료하기 ====
	@RequestMapping(value = "/editEnd.action", method = { RequestMethod.POST })
	public String editEnd(BoardVO boardvo, HttpServletRequest req) {

		String content = boardvo.getContent().replaceAll("\r\n", "<br/>");
		boardvo.setContent(content);

		/*
		 * 글 수정을 하려면 원본글의 암호와 수정시 입력해주는 암호가 일치할때만 글수정이 가능하도록 해야 한다.
		 */
		int n = service.edit(boardvo);
		// n 이 1 이면 update 성공
		// n 이 0 이면 update 실패(암호가 틀리므로)

		req.setAttribute("n", n);
		req.setAttribute("seq", boardvo.getSeq());

		return "board/editEnd.tiles2";
		// /Board/src/main/webapp/WEB-INF/views2/board/editEnd.jsp 파일을 생성한다.
	}

	// ==== #77. 글삭제 페이지 요청 ====
	@RequestMapping(value = "/del.action", method = { RequestMethod.GET })
	public String requireLogin_del(HttpServletRequest req, HttpServletResponse response) {

		// 삭제해야할 글번호 가져오기
		String seq = req.getParameter("seq");

		// 삭제되어질 글은 자신이 작성한 글이어야만 가능하다.
		// 삭제되어질 글내용을 읽어오면 작성자를 알 수 있다.
		BoardVO boardvo = service.getViewWithNoReadCount(seq);

		HttpSession session = req.getSession();
		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");

		if (!loginuser.getUserid().equals(boardvo.getUserid())) {
			String msg = "다른 사용자의 글은 삭제가 불가합니다.";
			String loc = "javascript:history.back()";

			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);

			return "msg.notiles";
		} else {
			// 삭제해야할 글번호를 request 영역에 저장시켜서 view 단 페이지로 넘긴다.
			req.setAttribute("seq", seq);

			// 글삭제시 글작성시 입력한 암호를 다시 입력받아 암호의 일치여부를 알아보도록
			// view단 페이지 del.jsp 로 넘긴다.
			return "board/del.tiles2";
			// /Board/src/main/webapp/WEB-INF/views2/board/del.jsp 파일을 생성한다.
		}

	}

	// ==== #78. 글삭제 페이지 완료 하기 ====
	@RequestMapping(value = "/delEnd.action", method = { RequestMethod.POST })
	public String delEnd(HttpServletRequest req) throws Throwable {

		/*
		 * 글 삭제를 하려면 원본글의 암호와 삭제시 입력해주는 암호가 일치할때만 삭제가 가능하도록 해야 한다. 서비스단에서 글삭제를 처리한 결과를
		 * int 타입으로 받아오겠다.
		 */
		String seq = req.getParameter("seq");
		String pw = req.getParameter("pw");

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seq", seq);
		map.put("pw", pw);

		int n = service.del(map);
		// 넘겨받은 값이 1(원게시글 및 딸린 댓글까지 삭제 성공)이면 글삭제 성공,
		// 넘겨받은 값이 2(딸린 댓글없는 경우 원게시글만 삭제 성공)이면 글삭제 성공,
		// 넘겨받은 값이 0이면 글삭제 실패(암호가 틀리므로)

		req.setAttribute("n", n);

		return "board/delEnd.tiles2";
		// /Board/src/main/webapp/WEB-INF/views2/board/delEnd.jsp 파일을 생성한다.
	}

	// ==== #85. 댓글쓰기 ====
	@RequestMapping(value = "/addComment.action", method = { RequestMethod.POST })
	public String requireLogin_addComment(HttpServletRequest req, HttpServletResponse response, CommentVO commentvo)
			throws Throwable {

		/*
		 * int n = service.addComment(commentvo);
		 * 
		 * if(n != 0) { // 댓글쓰기와 원게시물(tblBoard 테이블)에 댓글의 갯수(1씩 증가) // 증가가 성공했다라면
		 * req.setAttribute("msg", "댓글쓰기 완료!!"); } else { // 댓글쓰기를 실패 or 댓글의 갯수(1씩 증가)
		 * 증가가 실패 했다라면 req.setAttribute("msg", "댓글쓰기 실패!!"); }
		 * 
		 * String seq = commentvo.getParentSeq(); // 댓글에 대한 원게시물 글번호
		 * req.setAttribute("seq", seq);
		 * 
		 * return "board/addCommentEnd.tiles2"; //
		 * /Board/src/main/webapp/WEB-INF/views2/board/addCommentEnd.jsp 파일을 생성한다.
		 */
		// 댓글쓰기(**** AJAX로 처리 ****)
		int n = service.addComment(commentvo);

		JSONArray jsonarry = new JSONArray();
		String str_jsonarray = null;

		if (n != 0) {
			// 댓글쓰기와 원게시물(tblBoard 테이블)에 댓글의 갯수(1씩 증가)
			// 증가가 성공했다라면
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("name", commentvo.getName());
			jsonObj.put("content", commentvo.getContent());
			jsonObj.put("regDate", MyUtil.getNowTime());

			jsonarry.put(jsonObj);
		}

		str_jsonarray = jsonarry.toString();

		req.setAttribute("str_jsonarray", str_jsonarray);

		return "addCommentEndJSON.notiles";
		// /Board/src/main/webapp/WEB-INF/viewsnotiles/addCommentEndJSON.jsp 파일을 생성한다.

	}

	// ==== #148. 첨부파일 다운로드 받기
	@RequestMapping(value = "/download.action", method = { RequestMethod.GET })
	public void requireLogin_download(HttpServletRequest req, HttpServletResponse res) { // requireLogin_가 붙으면
																							// req,response가 있어야 한다.

		String seq = req.getParameter("seq");
		// 첨부파일이 있는 글번호를 받아옴

		BoardVO vo = service.getViewWithNoReadCount(seq); // 메소드 호출

		String fileName = vo.getFileName();
		String orgFilename = vo.getOrgFilename();

		// WAS(톰캣)의 webapp 의 절대경로를 알아와야 한다.
		HttpSession session = req.getSession();
		String root = session.getServletContext().getRealPath("/");
		String path = root + "resources" + File.separator + "files";
		// File.separator ==> 운영체제(OS)가 Windows 라면 "\" 이고,
		// 운영체제가 UNIX, Linux 라면 "/" 이다.

		boolean bool = false;

		bool = filemanager.doFileDownload(fileName, orgFilename, path, res);
		// 다운로드가 성공이면 true 를 반납해주고, 실패하면 false를 반납해준다.

		if (!bool) {
			// 다운로드가 실패할 경우 메시지를 띄워준다.
			res.setContentType("text/html; charset=UTF-8");
			PrintWriter writer = null;

			try {
				writer = res.getWriter();
				// 웹브라우저 상에 메시지를 쓰기 위한 객체생성
			} catch (Exception e) {

			}

			writer.println("<script type='text/javascript'>alert('파일 다운로드가 실패했습니다.')</script>");

		} // end of if()-----
	}

	// ==== #스마트에디터1. 단일사진 파일업로드 ====
	@RequestMapping(value = "/image/photoUpload.action", method = { RequestMethod.POST })
	public String photoUpload(PhotoVO photovo, HttpServletRequest req) {

		String callback = photovo.getCallback();
		String callback_func = photovo.getCallback_func();
		String file_result = "";

		if (!photovo.getFiledata().isEmpty()) {
			// 파일이 존재한다라면

			/*
			 * 1. 사용자가 보낸 파일을 WAS(톰캣)의 특정 폴더에 저장해주어야 한다. >>>> 파일이 업로드 되어질 특정 경로(폴더)지정해주기 우리는
			 * WAS 의 webapp/resources/photo_upload 라는 폴더로 지정해준다.
			 */

			// WAS 의 webapp 의 절대경로를 알아와야 한다.
			HttpSession session = req.getSession();
			String root = session.getServletContext().getRealPath("/");
			String path = root + "resources" + File.separator + "photo_upload";
			// path 가 첨부파일들을 저장할 WAS(톰캣)의 폴더가 된다.

			// System.out.println(">>>> 확인용 path ==> " + path);
			// >>>> 확인용 path ==>
			// C:\SpringWorkspaceTeach\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Board\resources\photo_upload

			// 2. 파일첨부를 위한 변수의 설정 및 값을 초기화한 후 파일올리기
			String newFilename = "";
			// WAS(톰캣) 디스크에 저장할 파일명

			byte[] bytes = null;
			// 첨부파일을 WAS(톰캣) 디스크에 저장할때 사용되는 용도

			try {
				bytes = photovo.getFiledata().getBytes();
				// getBytes()는 첨부된 파일을 바이트단위로 파일을 다 읽어오는 것이다.
				/*
				 * 2-1. 첨부된 파일을 읽어오는 것 첨부한 파일이 강아지.png 이라면 이파일을 WAS(톰캣) 디스크에 저장시키기 위해 byte[]
				 * 타입으로 변경해서 받아들인다.
				 */
				// 2-2. 이제 파일올리기를 한다.
				String original_name = photovo.getFiledata().getOriginalFilename();
				// photovo.getFiledata().getOriginalFilename() 은 첨부된 파일의 실제 파일명(문자열)을 얻어오는 것이다.
				newFilename = filemanager.doFileUpload(bytes, original_name, path);

				// System.out.println(">>>> 확인용 newFileName ==> " + newFileName);

				int width = filemanager.getImageWidth(path + File.separator + newFilename);
				// System.out.println("확인용 >>>>>>>> width : " + width);

				if (width > 600)
					width = 600;
				// System.out.println("확인용 >>>>>>>> width : " + width);

				String CP = req.getContextPath(); // board
				file_result += "&bNewLine=true&sFileName=" + newFilename + "&sWidth=" + width + "&sFileURL=" + CP
						+ "/resources/photo_upload/" + newFilename;

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			// 파일이 존재하지 않는다라면
			file_result += "&errstr=error";
		}

		return "redirect:" + callback + "?callback_func=" + callback_func + file_result;

	}// end of String photoUpload(PhotoVO photovo, HttpServletRequest
		// req)-------------------

	// ==== #스마트에디터2. 드래그앤드롭을 사용한 다중사진 파일업로드 ====
	@RequestMapping(value = "/image/multiplePhotoUpload.action", method = { RequestMethod.POST })
	public void multiplePhotoUpload(HttpServletRequest req, HttpServletResponse res) {

		/*
		 * 1. 사용자가 보낸 파일을 WAS(톰캣)의 특정 폴더에 저장해주어야 한다. >>>> 파일이 업로드 되어질 특정 경로(폴더)지정해주기 우리는
		 * WAS 의 webapp/resources/photo_upload 라는 폴더로 지정해준다.
		 */

		// WAS 의 webapp 의 절대경로를 알아와야 한다.
		HttpSession session = req.getSession();
		String root = session.getServletContext().getRealPath("/");
		String path = root + "resources" + File.separator + "photo_upload";
		// path 가 첨부파일들을 저장할 WAS(톰캣)의 폴더가 된다.

		// System.out.println(">>>> 확인용 path ==> " + path);
		// >>>> 확인용 path ==>
		// C:\SpringWorkspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Board\resources\photo_upload

		File dir = new File(path);
		if (!dir.exists())
			dir.mkdirs();

		String strURL = "";

		try {
			if (!"OPTIONS".equals(req.getMethod().toUpperCase())) {
				String filename = req.getHeader("file-name"); // 파일명을 받는다 - 일반 원본파일명

				// System.out.println(">>>> 확인용 filename ==> " + filename);
				// >>>> 확인용 filename ==> berkelekle%ED%8A%B8%EB%9E%9C%EB%94%9405.jpg

				InputStream is = req.getInputStream();
				/*
				 * 요청 헤더의 content-type이 application/json 이거나 multipart/form-data 형식일 때, 혹은 이름 없이
				 * 값만 전달될 때 이 값은 요청 헤더가 아닌 바디를 통해 전달된다. 이러한 형태의 값을 'payload body'라고 하는데 요청 바디에
				 * 직접 쓰여진다 하여 'request body post data'라고도 한다.
				 * 
				 * 서블릿에서 payload body는 Request.getParameter()가 아니라 Request.getInputStream() 혹은
				 * Request.getReader()를 통해 body를 직접 읽는 방식으로 가져온다.
				 */
				String newFilename = filemanager.doFileUpload(is, filename, path);

				int width = filemanager.getImageWidth(path + File.separator + newFilename);

				if (width > 600)
					width = 600;

				// System.out.println(">>>> 확인용 width ==> " + width);
				// >>>> 확인용 width ==> 600
				// >>>> 확인용 width ==> 121

				String CP = req.getContextPath(); // board

				strURL += "&bNewLine=true&sFileName=";
				strURL += newFilename;
				strURL += "&sWidth=" + width;
				strURL += "&sFileURL=" + CP + "/resources/photo_upload/" + newFilename;
			}

			/// 웹브라우저상에 사진 이미지를 쓰기 ///
			PrintWriter out = res.getWriter();
			out.print(strURL);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}// end of void multiplePhotoUpload(HttpServletRequest req, HttpServletResponse
		// res)----------------

	// ==== ***** 기상청 오픈 API XML 호출 ***** ==== //
	// 오픈 API 주소 : http://data.kma.go.kr/api/selectApiList.do

	// http://www.kma.go.kr/XML/weather/sfc_web_map.xml 을 사용하겠다
	@RequestMapping(value = "/weatherXML.action", method = { RequestMethod.GET })
	public String weatherXML() {

		return "xml/weatherXML";
	}

}
