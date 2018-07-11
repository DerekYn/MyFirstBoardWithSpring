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

// ===== #32. ��Ʈ�ѷ� ���� =====
@Controller
@Component
/*
 * XML���� ���� ����� ��ſ� Ŭ������ �տ� @Component ������̼��� �����ָ� �ش� Ŭ����(������ BoardController)��
 * bean���� �ڵ� ��ϵȴ�. �׸��� bean�� �̸�(ù���ڴ� �ҹ���)�� �ش� Ŭ������(������ BoardController)�� �ȴ�. ������
 * bean�� �̸��� boardController �� �ȴ�.
 */
public class BoardController {

	// ===== #33. ������ü �����ϱ�(DI : Dependency Injection) =====
	@Autowired
	private InterBoardService service;

	// ===== #132. ���Ͼ��ε� �� �ٿ�ε带 ���ִ� FileManager Ŭ���� ������ü �����ϱ�(DI : Dependency
	// Injection) =====
	@Autowired
	private FileManager filemanager;

	// ==== #40. ���� ������ ��û ====
	@RequestMapping(value = "/index.action", method = { RequestMethod.GET })
	public String index(HttpServletRequest req) {

		List<String> imgfilenameList = service.getImgfilenameList();

		req.setAttribute("imgfilenameList", imgfilenameList);

		return "main/index.tiles";
		// /Board/src/main/webapp/WEB-INF/views/main/index.jsp ������ �����Ѵ�.
	}

	// ==== #44. �α��� ������ ��û ====
	@RequestMapping(value = "/login.action", method = { RequestMethod.GET })
	public String login(HttpServletRequest req) {

		return "login/loginform.tiles";
		// /Board/src/main/webapp/WEB-INF/views/login/loginform.jsp ������ �����Ѵ�.
	}

	// ==== #45. �α��� ���� ��û ====
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
		// /Board/src/main/webapp/WEB-INF/views/login/loginEnd.jsp ������ �����Ѵ�.
	}

	// ==== #50. �α׾ƿ� �Ϸ� ��û ====
	@RequestMapping(value = "/logout.action", method = { RequestMethod.GET })
	public String logout(HttpServletRequest req) {

		HttpSession session = req.getSession();
		session.invalidate();

		return "login/logout.tiles";
		// /Board/src/main/webapp/WEB-INF/views/login/logout.jsp ������ �����Ѵ�.
	}

	// ===== #51. �۾��� form ������ ��û =====
	@RequestMapping(value = "/add.action", method = { RequestMethod.GET })
	public String requireLogin_add(HttpServletRequest req, HttpServletResponse res) {

		// ==== #121. �亯�۾��� �߰� �Ǿ����Ƿ� �Ʒ��� ���� �Ѵ�. (����) ====
		String fk_seq = req.getParameter("fk_seq"); // view.jsp���� �Ѱ��� ���� �޾ƿ���
		String groupno = req.getParameter("groupno");
		String depthno = req.getParameter("depthno");

		req.setAttribute("fk_seq", fk_seq);
		req.setAttribute("groupno", groupno);
		req.setAttribute("depthno", depthno);

		// ==== �亯�۾��� �߰� �Ǿ����Ƿ� �Ʒ��� ���� �Ѵ�. (��) ====

		return "board/add.tiles2";
		// /Board/src/main/webapp/WEB-INF/views2/board/add.jsp ������ �����Ѵ�.
	}

	// ===== #53-2. �۾��� �Ϸ� ��û =====
	@RequestMapping(value = "/addEnd.action", method = { RequestMethod.POST })
	// public String addEnd(BoardVO boardvo, HttpServletRequest req) { // boardvo ��
	// ���� �� fk_seq �� null �̶�� ���۾����̴�.
	/*
	 * ==== #136. ����÷�ΰ� �� �۾��� ==== ����, ���� ublic String addEnd(BoardVO boardvo,
	 * HttpServletRequest req) { �� �ּ�ó���� �� �� �Ʒ��� ���� �Ѵ�.
	 */
	public String addEnd(BoardVO boardvo, MultipartHttpServletRequest req, HttpSession session) { // �׳� Request�� ����÷��
																									// �Ұ�.
																									// MultipartHttpServletRequest
																									// �� ����ؾ� �Ѵ�.

		// ==== #137. ����ڰ� �� �ۿ� ������ ÷�ΰ� �� ������ �ƴϸ� ���� ���� ������ ������ ����� �Ѵ�. ====

		//////////////// === ÷�������� ������ ���Ͼ��ε� �ϱ� ���� === ////////////////
		if (!boardvo.getAttach().isEmpty()) {
			// attach�� ������� ������(��, ÷�������� �ִ� �����)

			/*
			 * 1. ����ڰ� ���� ������ WAS(��Ĺ)�� Ư�� ��� ������ �������־�� �Ѵ�. >>>> ������ ���ε� �Ǿ��� Ư�� ���(����)�������ֱ�
			 * �츮�� WAS(��Ĺ)�� webapp/resources/files ��� ������ �������ְڴ�.
			 */

			// WAS(��Ĺ)�� webapp �� �����θ� �˾ƿ;� �Ѵ�.
			String root = session.getServletContext().getRealPath("/");
			String path = root + "resources" + File.separator + "files";
			// File.separator ==> �ü��(OS)�� Windows ��� "\" �̰�,
			// �ü���� UNIX, Linux ��� "/" �̴�.

			System.out.println("Ȯ�ο� path : " + path);
			// Ȯ�ο� path :
			// C:\springworkspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Board\resources\files

			// 2. ����÷�θ� ���� ������ ���� �� ���� �ʱ�ȭ�� �� ���Ͽø���
			String newFileName = ""; // WAS(��Ĺ) ��ũ�� ������ ���ϸ�.

			byte[] bytes = null; // ÷�������� WAS(��Ĺ) ��ũ�� ������ �� ���Ǵ� �뵵.

			long fileSize = 0; // ����ũ�⸦ �о���� ���� �뵵

			try { // FileManager.java���� throws Exception �Ǿ��ִ�. �׷��� �ϴ°���
				bytes = boardvo.getAttach().getBytes();
				// getBytes() ==> ÷�ε� ������ ����Ʈ������ ������ �� �о���� ��

				newFileName = filemanager.doFileUpload(bytes, boardvo.getAttach().getOriginalFilename(), path);
				// ������ ���ε� �� �� ����ð�+����Ÿ��.Ȯ���ڷ� �Ǿ��� ���ϸ���
				// ���Ϲ޾� newFileName ���� �����Ѵ�.
				// boardvo.getAttach().getOriginalFilename() �� ÷�ε� ������ ���� ���ϸ�(���ڿ�)�� ������ ���̴�.

				boardvo.setFileName(newFileName);
				// newFileName �� WAS(��Ĺ)�� ����� ���ϸ�(ex.201806283242343422424585453454547341.png)

				boardvo.setOrgFilename(boardvo.getAttach().getOriginalFilename());
				// boardvo.getAttach().getOriginalFilename() �� ��¥ ���ϸ�(ex. ������.png)
				// ����ڰ� ������ �ٿ�ε��� �� ���Ǿ����� ���ϸ�

				fileSize = boardvo.getAttach().getSize();
				// boardvo.getAttach().getSize() �� ÷���� ������ ũ��
				// Ÿ���� long �̴�.

				boardvo.setFileSize(String.valueOf(fileSize)); // String Ÿ������ ���� �� vo�� �־��ش�.

			} catch (Exception e) {

			}

		}

		//////////////// === ÷�������� ������ ���Ͼ��ε� �ϱ� �� === /////////////////

		String content = boardvo.getContent().replaceAll("\r\n", "<br/>");
		boardvo.setContent(content);

		// int n = service.add(boardvo);
		// ==== #138. ����÷�ΰ� �ִ� �۾��� �̹Ƿ�
		// ���� ���� �޼ҵ带 �ּ�ó�� �ϰ� �Ʒ��� �����Ѵ�.

		int n = 0;

		if (boardvo.getAttach().isEmpty()) { // �ֺ����
			// ����÷�ΰ� ���ٸ�
			n = service.add(boardvo);
		}

		if (!boardvo.getAttach().isEmpty()) {
			// ����÷�ΰ� �ִٸ�
			n = service.add_withFile(boardvo);
		}

		req.setAttribute("n", n);

		return "board/addEnd.tiles2";
		// /Board/src/main/webapp/WEB-INF/views2/board/addEnd.jsp ������ �����Ѵ�.
	}

	// ===== #57. �۸�� ���� ������ ��û =====
	@RequestMapping(value = "/list.action", method = { RequestMethod.GET })
	public String list(HttpServletRequest req) {

		// ==== #68. ����ȸ��(readCount)����(DML�� update)��
		// �ݵ�� �ش� �������� Ŭ������ ��쿡�� �����ǰ�
		// ������������ ���ΰ�ħ(F5)�� ���� ��쿡�� ������ �ȵǵ��� �Ѵ�.
		// �̰��� �ϱ����� ǥ���� �ܴ�. === ���� ===
		HttpSession session = req.getSession();
		session.setAttribute("readCountPermission", "yes");
		// �ݵ�� �������� �ּ�â�� /list.action �̶�� �Է��ؾ߸�
		// ���ǿ� "readCountPermission" ���� ����Ǿ��ִ�.
		//// === �� ===

		List<BoardVO> boardList = null;

		// boardList = service.boardList(); // �˻�� ���� ��� ��ü ��ȸ���ִ� ��

		// ==== #106. �˻�� ���ԵǾ����Ƿ�
		// ���� ���� boardList = service.boardList(); �κ���
		// �ּ�ó���ϰ� �Ʒ��� �۾��� �Ѵ�. ====
		String colname = req.getParameter("colname");
		String search = req.getParameter("search");

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("colname", colname);
		map.put("search", search);

		/*
		 * // === ����¡ ó�� �� �Ѱ� === // if( (colname != null && search != null) &&
		 * (!colname.equals("null") && !search.equals("null")) &&
		 * (!colname.trim().isEmpty() && !search.trim().isEmpty()) ) { boardList =
		 * service.boardList2(map); // �˻�� �ִ� �� } else { boardList =
		 * service.boardList(map); // �˻�� ���� �� }
		 * 
		 * req.setAttribute("boardList", boardList); req.setAttribute("colname",
		 * colname); req.setAttribute("search", search);
		 */

		// ===== #110. ����¡ ó�� �ϱ� =====
		String str_currentShowPageNo = req.getParameter("currentShowPageNo");

		int totalCount = 0; // �ѰԽù��Ǽ�
		int sizePerPage = 5; // �� �������� ������ �Խù� �Ǽ�
		int currentShowPageNo = 0; // ���� �����ִ� ������ ��ȣ�μ�, �ʱ�ġ�δ� 1�������� ������.
		int totalPage = 0; // ���������� (���������� ������ �� ������ ����)

		int startRno = 0; // ������ ��ȣ
		int endRno = 0; // ���� ��ȣ

		int blockSize = 10; // "��������" �� ������ �������� ����

		/*
		 * ==== ���������� ���ϱ� ==== �˻������� ���� ���� �������� ���� �˻������� ���� ���� �������� ���� ���ؾ� �Ѵ�.
		 * 
		 * �˻������� ���� ���� �������� �� ==> colname �� search ���� null �� ���̰�, �˻������� ���� ���� �������� �� ==>
		 * colname �� search ���� null �� �ƴ� ���̴�.
		 */
		// ���� �ѰԽù� �Ǽ��� ���Ѵ�.
		if ((colname != null && search != null) && (!colname.equals("null") && !search.equals("null"))
				&& (!colname.trim().isEmpty() && !search.trim().isEmpty())) {
			totalCount = service.getTotalCount2(map); // �˻�� �ִ� �ѰԽù� �Ǽ�
		} else {
			totalCount = service.getTotalCount(); // �˻�� ���� �ѰԽù� �Ǽ�
		}

		totalPage = (int) Math.ceil((double) totalCount / sizePerPage);

		if (str_currentShowPageNo == null) {
			// �Խ��� �ʱ�ȭ�鿡 �������� ����
			// req.getParameter("currentShowPageNo"); ���� �����Ƿ�
			// str_currentShowPageNo �� null �� �ȴ�.

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

		// **** ������ �Խñ��� ������ ���Ѵ�.(������!!!) ****
		/*
		 * currentShowPageNo startRno endRno --------------------------------------- 1
		 * page ==> 1 5 2 page ==> 6 10 3 page ==> 11 15 4 page ==> 16 20 5 page ==> 21
		 * 25 6 page ==> 26 30 7 page ==> 31 35
		 */

		startRno = (currentShowPageNo - 1) * sizePerPage + 1;
		endRno = startRno + sizePerPage - 1;

		// ==== #111. ����¡ ó���� ���� startRno, endRno �� map �� �߰��Ͽ�
		// �Ķ���ͷ� �Ѱܼ� select �ǵ��� �Ѵ�.
		map.put("startRno", String.valueOf(startRno));
		map.put("endRno", String.valueOf(endRno));

		if ((colname != null && search != null) && (!colname.equals("null") && !search.equals("null"))
				&& (!colname.trim().isEmpty() && !search.trim().isEmpty())) {
			boardList = service.boardList2(map); // �˻�� �ִ� ����¡ó��
		} else {
			boardList = service.boardList(map); // �˻�� ���� ����¡ó��
		}

		// ==== #113. �������� �����
		// (����, �������ٿ� ��Ÿ�� �������� ���� ���ؾ� �ϴµ� �츮�� ������ ���ߴ�.) =====

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
		// /Board/src/main/webapp/WEB-INF/views2/board/list.jsp ������ �����Ѵ�.
	}

	// ==== #61. ��1���� �����ִ� ������ ��û�ϱ� ====
	@RequestMapping(value = "/view.action", method = { RequestMethod.GET })
	public String view(HttpServletRequest req) {

		String seq = req.getParameter("seq"); // �۹�ȣ �޾ƿ���
		String goBackURL = req.getParameter("goBackURL"); // �����ּ� ��������

		BoardVO boardvo = null;

		// ==== #67. ����ȸ��(readCount)����(DML�� update)��
		// �ݵ�� �ش� �������� Ŭ������ ��쿡�� �����ǰ�
		// ������������ ���ΰ�ħ(F5)�� ���� ��쿡�� ������ �ȵǵ��� �Ѵ�.
		HttpSession session = req.getSession();

		if ("yes".equals(session.getAttribute("readCountPermission"))) {

			MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
			String userid = null;

			if (loginuser != null) {
				userid = loginuser.getUserid();
			}

			boardvo = service.getView(seq, userid);
			// ��ȸ��(readCount) ������ �Ŀ� ��1���� �������� ��.
			// ��, �ڽ��� �� ���� �ڽ��� �����ÿ��� ��ȸ���� �������� �ʰ�,
			// �ٸ� ����� �� ���̶�� ��ȸ���� �����ǵ��� �ؾ� �Ѵ�.
			// �α��� ���� ���� ���¿��� ���� ������ ��ȸ�� ������ �Ͼ�� �ʵ��� �Ѵ�.

			session.removeAttribute("readCountPermission");
		} else { // ������������ ���ΰ�ħ(F5)�� ���� ���

			// ��ȸ�� 1���� ���� �׳� �� 1���� �������� ��
			boardvo = service.getViewWithNoReadCount(seq);
		}

		req.setAttribute("boardvo", boardvo);

		// ==== #91. ��� ���� �������� ====
		List<CommentVO> commentList = service.listComment(seq);
		req.setAttribute("commentList", commentList);
		req.setAttribute("goBackURL", goBackURL);

		return "board/view.tiles2";
		// /Board/src/main/webapp/WEB-INF/views2/board/view.jsp ������ �����Ѵ�.
	}

	// ==== #70. �ۼ��� ������ ��û ====
	@RequestMapping(value = "/edit.action", method = { RequestMethod.GET })
	public String requireLogin_edit(HttpServletRequest req, HttpServletResponse response) {

		String seq = req.getParameter("seq"); // �����ؾ��� �۹�ȣ ��������

		// �����ؾ��� �� ��ü ���� ��������
		// ��ȸ�� 1���� ���� �׳� �� 1���� �������� ��
		BoardVO boardvo = service.getViewWithNoReadCount(seq);

		HttpSession session = req.getSession();
		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");

		if (!boardvo.getUserid().equals(loginuser.getUserid())) {
			String msg = "�ٸ� ������� ���� ������ �Ұ��մϴ�.";
			String loc = "javascript:history.back()";

			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);

			return "msg.notiles";
			// /Board/src/main/webapp/WEB-INF/viewsnotiles/msg.jsp ������ �����Ѵ�.
		} else {
			// ������ 1������ request ������ ������Ѽ� view �� �������� �ѱ��.
			req.setAttribute("boardvo", boardvo);

			return "board/edit.tiles2";
			// /Board/src/main/webapp/WEB-INF/views2/board/edit.jsp ������ �����Ѵ�.
		}

	}

	// ==== #71. �ۼ��� ������ �Ϸ��ϱ� ====
	@RequestMapping(value = "/editEnd.action", method = { RequestMethod.POST })
	public String editEnd(BoardVO boardvo, HttpServletRequest req) {

		String content = boardvo.getContent().replaceAll("\r\n", "<br/>");
		boardvo.setContent(content);

		/*
		 * �� ������ �Ϸ��� �������� ��ȣ�� ������ �Է����ִ� ��ȣ�� ��ġ�Ҷ��� �ۼ����� �����ϵ��� �ؾ� �Ѵ�.
		 */
		int n = service.edit(boardvo);
		// n �� 1 �̸� update ����
		// n �� 0 �̸� update ����(��ȣ�� Ʋ���Ƿ�)

		req.setAttribute("n", n);
		req.setAttribute("seq", boardvo.getSeq());

		return "board/editEnd.tiles2";
		// /Board/src/main/webapp/WEB-INF/views2/board/editEnd.jsp ������ �����Ѵ�.
	}

	// ==== #77. �ۻ��� ������ ��û ====
	@RequestMapping(value = "/del.action", method = { RequestMethod.GET })
	public String requireLogin_del(HttpServletRequest req, HttpServletResponse response) {

		// �����ؾ��� �۹�ȣ ��������
		String seq = req.getParameter("seq");

		// �����Ǿ��� ���� �ڽ��� �ۼ��� ���̾�߸� �����ϴ�.
		// �����Ǿ��� �۳����� �о���� �ۼ��ڸ� �� �� �ִ�.
		BoardVO boardvo = service.getViewWithNoReadCount(seq);

		HttpSession session = req.getSession();
		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");

		if (!loginuser.getUserid().equals(boardvo.getUserid())) {
			String msg = "�ٸ� ������� ���� ������ �Ұ��մϴ�.";
			String loc = "javascript:history.back()";

			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);

			return "msg.notiles";
		} else {
			// �����ؾ��� �۹�ȣ�� request ������ ������Ѽ� view �� �������� �ѱ��.
			req.setAttribute("seq", seq);

			// �ۻ����� ���ۼ��� �Է��� ��ȣ�� �ٽ� �Է¹޾� ��ȣ�� ��ġ���θ� �˾ƺ�����
			// view�� ������ del.jsp �� �ѱ��.
			return "board/del.tiles2";
			// /Board/src/main/webapp/WEB-INF/views2/board/del.jsp ������ �����Ѵ�.
		}

	}

	// ==== #78. �ۻ��� ������ �Ϸ� �ϱ� ====
	@RequestMapping(value = "/delEnd.action", method = { RequestMethod.POST })
	public String delEnd(HttpServletRequest req) throws Throwable {

		/*
		 * �� ������ �Ϸ��� �������� ��ȣ�� ������ �Է����ִ� ��ȣ�� ��ġ�Ҷ��� ������ �����ϵ��� �ؾ� �Ѵ�. ���񽺴ܿ��� �ۻ����� ó���� �����
		 * int Ÿ������ �޾ƿ��ڴ�.
		 */
		String seq = req.getParameter("seq");
		String pw = req.getParameter("pw");

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seq", seq);
		map.put("pw", pw);

		int n = service.del(map);
		// �Ѱܹ��� ���� 1(���Խñ� �� ���� ��۱��� ���� ����)�̸� �ۻ��� ����,
		// �Ѱܹ��� ���� 2(���� ��۾��� ��� ���Խñ۸� ���� ����)�̸� �ۻ��� ����,
		// �Ѱܹ��� ���� 0�̸� �ۻ��� ����(��ȣ�� Ʋ���Ƿ�)

		req.setAttribute("n", n);

		return "board/delEnd.tiles2";
		// /Board/src/main/webapp/WEB-INF/views2/board/delEnd.jsp ������ �����Ѵ�.
	}

	// ==== #85. ��۾��� ====
	@RequestMapping(value = "/addComment.action", method = { RequestMethod.POST })
	public String requireLogin_addComment(HttpServletRequest req, HttpServletResponse response, CommentVO commentvo)
			throws Throwable {

		/*
		 * int n = service.addComment(commentvo);
		 * 
		 * if(n != 0) { // ��۾���� ���Խù�(tblBoard ���̺�)�� ����� ����(1�� ����) // ������ �����ߴٶ��
		 * req.setAttribute("msg", "��۾��� �Ϸ�!!"); } else { // ��۾��⸦ ���� or ����� ����(1�� ����)
		 * ������ ���� �ߴٶ�� req.setAttribute("msg", "��۾��� ����!!"); }
		 * 
		 * String seq = commentvo.getParentSeq(); // ��ۿ� ���� ���Խù� �۹�ȣ
		 * req.setAttribute("seq", seq);
		 * 
		 * return "board/addCommentEnd.tiles2"; //
		 * /Board/src/main/webapp/WEB-INF/views2/board/addCommentEnd.jsp ������ �����Ѵ�.
		 */
		// ��۾���(**** AJAX�� ó�� ****)
		int n = service.addComment(commentvo);

		JSONArray jsonarry = new JSONArray();
		String str_jsonarray = null;

		if (n != 0) {
			// ��۾���� ���Խù�(tblBoard ���̺�)�� ����� ����(1�� ����)
			// ������ �����ߴٶ��
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("name", commentvo.getName());
			jsonObj.put("content", commentvo.getContent());
			jsonObj.put("regDate", MyUtil.getNowTime());

			jsonarry.put(jsonObj);
		}

		str_jsonarray = jsonarry.toString();

		req.setAttribute("str_jsonarray", str_jsonarray);

		return "addCommentEndJSON.notiles";
		// /Board/src/main/webapp/WEB-INF/viewsnotiles/addCommentEndJSON.jsp ������ �����Ѵ�.

	}

	// ==== #148. ÷������ �ٿ�ε� �ޱ�
	@RequestMapping(value = "/download.action", method = { RequestMethod.GET })
	public void requireLogin_download(HttpServletRequest req, HttpServletResponse res) { // requireLogin_�� ������
																							// req,response�� �־�� �Ѵ�.

		String seq = req.getParameter("seq");
		// ÷�������� �ִ� �۹�ȣ�� �޾ƿ�

		BoardVO vo = service.getViewWithNoReadCount(seq); // �޼ҵ� ȣ��

		String fileName = vo.getFileName();
		String orgFilename = vo.getOrgFilename();

		// WAS(��Ĺ)�� webapp �� �����θ� �˾ƿ;� �Ѵ�.
		HttpSession session = req.getSession();
		String root = session.getServletContext().getRealPath("/");
		String path = root + "resources" + File.separator + "files";
		// File.separator ==> �ü��(OS)�� Windows ��� "\" �̰�,
		// �ü���� UNIX, Linux ��� "/" �̴�.

		boolean bool = false;

		bool = filemanager.doFileDownload(fileName, orgFilename, path, res);
		// �ٿ�ε尡 �����̸� true �� �ݳ����ְ�, �����ϸ� false�� �ݳ����ش�.

		if (!bool) {
			// �ٿ�ε尡 ������ ��� �޽����� ����ش�.
			res.setContentType("text/html; charset=UTF-8");
			PrintWriter writer = null;

			try {
				writer = res.getWriter();
				// �������� �� �޽����� ���� ���� ��ü����
			} catch (Exception e) {

			}

			writer.println("<script type='text/javascript'>alert('���� �ٿ�ε尡 �����߽��ϴ�.')</script>");

		} // end of if()-----
	}

	// ==== #����Ʈ������1. ���ϻ��� ���Ͼ��ε� ====
	@RequestMapping(value = "/image/photoUpload.action", method = { RequestMethod.POST })
	public String photoUpload(PhotoVO photovo, HttpServletRequest req) {

		String callback = photovo.getCallback();
		String callback_func = photovo.getCallback_func();
		String file_result = "";

		if (!photovo.getFiledata().isEmpty()) {
			// ������ �����Ѵٶ��

			/*
			 * 1. ����ڰ� ���� ������ WAS(��Ĺ)�� Ư�� ������ �������־�� �Ѵ�. >>>> ������ ���ε� �Ǿ��� Ư�� ���(����)�������ֱ� �츮��
			 * WAS �� webapp/resources/photo_upload ��� ������ �������ش�.
			 */

			// WAS �� webapp �� �����θ� �˾ƿ;� �Ѵ�.
			HttpSession session = req.getSession();
			String root = session.getServletContext().getRealPath("/");
			String path = root + "resources" + File.separator + "photo_upload";
			// path �� ÷�����ϵ��� ������ WAS(��Ĺ)�� ������ �ȴ�.

			// System.out.println(">>>> Ȯ�ο� path ==> " + path);
			// >>>> Ȯ�ο� path ==>
			// C:\SpringWorkspaceTeach\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Board\resources\photo_upload

			// 2. ����÷�θ� ���� ������ ���� �� ���� �ʱ�ȭ�� �� ���Ͽø���
			String newFilename = "";
			// WAS(��Ĺ) ��ũ�� ������ ���ϸ�

			byte[] bytes = null;
			// ÷�������� WAS(��Ĺ) ��ũ�� �����Ҷ� ���Ǵ� �뵵

			try {
				bytes = photovo.getFiledata().getBytes();
				// getBytes()�� ÷�ε� ������ ����Ʈ������ ������ �� �о���� ���̴�.
				/*
				 * 2-1. ÷�ε� ������ �о���� �� ÷���� ������ ������.png �̶�� �������� WAS(��Ĺ) ��ũ�� �����Ű�� ���� byte[]
				 * Ÿ������ �����ؼ� �޾Ƶ��δ�.
				 */
				// 2-2. ���� ���Ͽø��⸦ �Ѵ�.
				String original_name = photovo.getFiledata().getOriginalFilename();
				// photovo.getFiledata().getOriginalFilename() �� ÷�ε� ������ ���� ���ϸ�(���ڿ�)�� ������ ���̴�.
				newFilename = filemanager.doFileUpload(bytes, original_name, path);

				// System.out.println(">>>> Ȯ�ο� newFileName ==> " + newFileName);

				int width = filemanager.getImageWidth(path + File.separator + newFilename);
				// System.out.println("Ȯ�ο� >>>>>>>> width : " + width);

				if (width > 600)
					width = 600;
				// System.out.println("Ȯ�ο� >>>>>>>> width : " + width);

				String CP = req.getContextPath(); // board
				file_result += "&bNewLine=true&sFileName=" + newFilename + "&sWidth=" + width + "&sFileURL=" + CP
						+ "/resources/photo_upload/" + newFilename;

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			// ������ �������� �ʴ´ٶ��
			file_result += "&errstr=error";
		}

		return "redirect:" + callback + "?callback_func=" + callback_func + file_result;

	}// end of String photoUpload(PhotoVO photovo, HttpServletRequest
		// req)-------------------

	// ==== #����Ʈ������2. �巡�׾ص���� ����� ���߻��� ���Ͼ��ε� ====
	@RequestMapping(value = "/image/multiplePhotoUpload.action", method = { RequestMethod.POST })
	public void multiplePhotoUpload(HttpServletRequest req, HttpServletResponse res) {

		/*
		 * 1. ����ڰ� ���� ������ WAS(��Ĺ)�� Ư�� ������ �������־�� �Ѵ�. >>>> ������ ���ε� �Ǿ��� Ư�� ���(����)�������ֱ� �츮��
		 * WAS �� webapp/resources/photo_upload ��� ������ �������ش�.
		 */

		// WAS �� webapp �� �����θ� �˾ƿ;� �Ѵ�.
		HttpSession session = req.getSession();
		String root = session.getServletContext().getRealPath("/");
		String path = root + "resources" + File.separator + "photo_upload";
		// path �� ÷�����ϵ��� ������ WAS(��Ĺ)�� ������ �ȴ�.

		// System.out.println(">>>> Ȯ�ο� path ==> " + path);
		// >>>> Ȯ�ο� path ==>
		// C:\SpringWorkspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Board\resources\photo_upload

		File dir = new File(path);
		if (!dir.exists())
			dir.mkdirs();

		String strURL = "";

		try {
			if (!"OPTIONS".equals(req.getMethod().toUpperCase())) {
				String filename = req.getHeader("file-name"); // ���ϸ��� �޴´� - �Ϲ� �������ϸ�

				// System.out.println(">>>> Ȯ�ο� filename ==> " + filename);
				// >>>> Ȯ�ο� filename ==> berkelekle%ED%8A%B8%EB%9E%9C%EB%94%9405.jpg

				InputStream is = req.getInputStream();
				/*
				 * ��û ����� content-type�� application/json �̰ų� multipart/form-data ������ ��, Ȥ�� �̸� ����
				 * ���� ���޵� �� �� ���� ��û ����� �ƴ� �ٵ� ���� ���޵ȴ�. �̷��� ������ ���� 'payload body'��� �ϴµ� ��û �ٵ�
				 * ���� �������� �Ͽ� 'request body post data'��� �Ѵ�.
				 * 
				 * �������� payload body�� Request.getParameter()�� �ƴ϶� Request.getInputStream() Ȥ��
				 * Request.getReader()�� ���� body�� ���� �д� ������� �����´�.
				 */
				String newFilename = filemanager.doFileUpload(is, filename, path);

				int width = filemanager.getImageWidth(path + File.separator + newFilename);

				if (width > 600)
					width = 600;

				// System.out.println(">>>> Ȯ�ο� width ==> " + width);
				// >>>> Ȯ�ο� width ==> 600
				// >>>> Ȯ�ο� width ==> 121

				String CP = req.getContextPath(); // board

				strURL += "&bNewLine=true&sFileName=";
				strURL += newFilename;
				strURL += "&sWidth=" + width;
				strURL += "&sFileURL=" + CP + "/resources/photo_upload/" + newFilename;
			}

			/// ���������� ���� �̹����� ���� ///
			PrintWriter out = res.getWriter();
			out.print(strURL);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}// end of void multiplePhotoUpload(HttpServletRequest req, HttpServletResponse
		// res)----------------

	// ==== ***** ���û ���� API XML ȣ�� ***** ==== //
	// ���� API �ּ� : http://data.kma.go.kr/api/selectApiList.do

	// http://www.kma.go.kr/XML/weather/sfc_web_map.xml �� ����ϰڴ�
	@RequestMapping(value = "/weatherXML.action", method = { RequestMethod.GET })
	public String weatherXML() {

		return "xml/weatherXML";
	}

}
