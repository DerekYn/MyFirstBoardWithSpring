package com.spring.product.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.spring.common.ThumbnailManager;
import com.spring.mail.GoogleMail;
import com.spring.member.model.MemberVO;
import com.spring.product.service.InterProductService;
import com.spring.common.FileManager;

@Controller
@Component
public class ProductController {

	@Autowired
	private InterProductService service;
	
	@Autowired
	private FileManager fileManager;
	
	// ===== #167. ������� �ٷ���ִ� Ŭ���� ������ü �����ϱ�(DI: Dependency Injection) =====
	@Autowired
	private ThumbnailManager thumbnailManager;
	
	// ===== #168. ���������� ÷������ �� �ִ� ��ǰ��� �������� ��û.
	@RequestMapping(value="/product/addProduct.action", method={RequestMethod.GET} ) 
	public String requireLogin_addProduct(HttpServletRequest req, HttpServletResponse res) {
		
		HttpSession session = req.getSession();
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
		int gradelevel = loginuser.getGradelevel();
		
		if(gradelevel < 10) {
			session.invalidate();
			
			String msg = "������ �Ұ��մϴ�.";
			String loc = "javascript:history.back();";
			
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			return "msg.notiles";
		}
		else {
			  List<HashMap<String, String>> catecodeList = service.getCatecode();
			  
			  List<HashMap<String, String>> speccodeList = service.getSpeccode();
			  			  
			  req.setAttribute("catecodeList", catecodeList);
			  
			  req.setAttribute("speccodeList", speccodeList);
			
			  return "product/addProduct.tiles2";  
			  // ==> /Board/src/main/webapp/WEB-INF/views2/product/addProduct.jsp ������ �����Ѵ�.	
		}
	}
	
	
	@RequestMapping(value="/product/addProductEnd.action", method={RequestMethod.POST} ) 
	public String addDoubleFileEnd(MultipartHttpServletRequest req, HttpServletResponse res) {
		
		String prodseq = String.valueOf(service.getProdseq()); // ���� ��ǰ�Ϸù�ȣ ä���ؿ���
		String prodname = req.getParameter("prodname");
		String fk_catecode = req.getParameter("fk_catecode");
		String prodcompany = req.getParameter("prodcompany");
		String price = req.getParameter("price");
		String saleprice = req.getParameter("saleprice");
		String fk_speccode = req.getParameter("fk_speccode");
		String prodcontent = req.getParameter("prodcontent");
		String prodpoint = req.getParameter("prodpoint");
		List<MultipartFile> attachList = req.getFiles("attach");
		
		HashMap<String, String> mapProduct = new HashMap<String, String>();
		mapProduct.put("prodseq", prodseq);
		mapProduct.put("prodname", prodname);
		mapProduct.put("fk_catecode", fk_catecode);
		mapProduct.put("prodcompany", prodcompany);
		mapProduct.put("price", price);
		mapProduct.put("saleprice", saleprice);
		mapProduct.put("fk_speccode", fk_speccode);
		mapProduct.put("prodcontent", prodcontent);
		mapProduct.put("prodpoint", prodpoint);
		
		List<HashMap<String, String>> mapProductimageList = new ArrayList<HashMap<String, String>>();
		if(attachList != null) {
			// WAS �� webapp �� �����θ� �˾ƿ;� �Ѵ�. 
			HttpSession session = req.getSession();
			String root = session.getServletContext().getRealPath("/"); 
			String path = root + "resources"+File.separator+"files";
			// path �� ÷�����ϵ��� ������ WAS(��Ĺ)�� ������ �ȴ�. 
			
			String newFileName = "";
			// WAS(��Ĺ) ��ũ�� ������ ���ϸ� 
			
			byte[] bytes = null;
			// ÷�������� WAS(��Ĺ) ��ũ�� �����Ҷ� ���Ǵ� �뵵 
			
			long fileSize = 0;
			// ����ũ�⸦ �о���� ���� �뵵
			
			String thumbnailFileName = ""; 
			// WAS ��ũ�� ����� thumbnail ���ϸ� 
			
			for(int i=0; i<attachList.size(); i++) {
				try {
					 bytes = attachList.get(i).getBytes();
					 
					 newFileName = fileManager.doFileUpload(bytes, attachList.get(i).getOriginalFilename(), path); 
					 
					 fileSize = attachList.get(i).getSize();
					 
					// === >>>> thumbnail ���� ������ ���� �۾� <<<<    =========  //
					 thumbnailFileName = thumbnailManager.doCreateThumbnail(newFileName, path); 
					// ===================================================  //
					 
					 HashMap<String, String> mapProductimage = new HashMap<String, String>();
					 
					 mapProductimage.put("fk_prodseq", prodseq);
					 mapProductimage.put("imagefilename", newFileName);
					 mapProductimage.put("imageorgFilename", attachList.get(i).getOriginalFilename());
					 mapProductimage.put("imagefileSize", String.valueOf(fileSize));
					 mapProductimage.put("thumbnailFileName", thumbnailFileName);
					 
					 mapProductimageList.add(mapProductimage);
					 
				} catch (Exception e) {
					
				}
				
			}// end of for-------------------------
			
		}// end of if------------------------------
		
		
	  // **** ������ �Է¹��� ��ǰ�Է����� ���� 
	  //      Service ������ �Ѱܼ� ���̺� insert �ϱ�� �Ѵ�.
		   
	       // �̹�������÷�ΰ� ���� ��� �Ǵ� �̹�������÷�ΰ� �ִ� ���� �����
		   // Service ������ ȣ���ϱ�
		   int n = 0;
		   int m = 0;
		   int count = 0;
		   
		   if(attachList == null) { // ���� ÷�εȰ��� ���ٸ�
			   n = service.addProduct(mapProduct);
		   }
		   else if(attachList != null) { // ���� ÷�εȰ��� �ִٸ�
			   n = service.addProduct(mapProduct);
			   
			   for(int i=0; i<mapProductimageList.size(); i++) {
				   m = service.addProductimage(mapProductimageList.get(i));
				   if(m==1) count++;
			   }
			   
			   if(mapProductimageList.size() == count) {
				   n=1;
			   }
			   else {
				   n=0;
			   }
		   }
		   			   
		   String msg = "";
		   String loc = "";
		   if(n==1) {
			   msg = "��ǰ�Է� ����!!";
			   loc = "/board/product/listProduct.action;";
		   }
		   else {
			   msg = "��ǰ�Է� ����!!";
			   loc = "javascript:history.back();";
		   }
		    		
		   req.setAttribute("msg", msg);
		   req.setAttribute("loc", loc);
			
		   return "msg.notiles";
	}
	

	// === ��ǰ�԰� �������� ��û�ϱ� ===
	@RequestMapping(value="/product/productStore.action", method={RequestMethod.GET} ) 
	public String requireLogin_productStore(HttpServletRequest req, HttpServletResponse res) {
		
		HttpSession session = req.getSession();
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
		int gradelevel = loginuser.getGradelevel();
		
		if(gradelevel < 10) {
			session.invalidate();
			
			String msg = "������ �Ұ��մϴ�.";
			String loc = "javascript:history.back();";
			
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			return "msg.notiles";
		}
		else {
			List<HashMap<String, String>> prodseqList = service.getProdseqList();
			req.setAttribute("prodseqList", prodseqList);
		
			return "product/productStore.tiles2";
		}
	}
	
	
	// === ��ǰ�԰� �Ϸ� ��û�ϱ� ===
	@RequestMapping(value="/product/productStoreEnd.action", method={RequestMethod.POST} ) 
	public String productStoreEnd(HttpServletRequest req, HttpServletResponse res) 
			throws Throwable {
		
		String prodseq = req.getParameter("prodseq");
		String ibgoqty = req.getParameter("ibgoqty");
		
		HashMap<String, String> mapIbgo = new HashMap<String, String>();
		mapIbgo.put("prodseq", prodseq);
		mapIbgo.put("ibgoqty", ibgoqty);
		
		int n = service.addproductStore(mapIbgo);
				   			   
		String msg = "";
		String loc = "";
		if(n==1) {
		   msg = "�԰� ����!!";
		   loc = "/board/product/listProduct.action;";
		   
		// ======= ***** ��ǰ �԰� �Ϸ�Ǿ��ٶ�� email ������ ���� ***** ======= //
		   	GoogleMail mail = new GoogleMail();
		   	
		    // ��ǰ��ȣ�� �ش��ϴ� ��ǰ��, �����縦 ����.
		   	HashMap<String, String> viewProductmap = service.getviewProduct(prodseq);
		   	String PRODNAME = viewProductmap.get("PRODNAME");
		   	String PRODCOMPANY = viewProductmap.get("PRODCOMPANY");
				
		    // ��ǰ��ȣ�� �ش��ϴ� ������̹��� ���ϸ��� ����.
			List<HashMap<String, String>> viewProductImageList = service.getviewProductImage(prodseq);
			String[] thumbnailfilenameArr = new String[viewProductImageList.size()]; 
						
			for(int i=0; i<viewProductImageList.size(); i++) {
				thumbnailfilenameArr[i] = viewProductImageList.get(i).get("THUMBNAILFILENAME");
			}
			
			StringBuilder sb = new StringBuilder();
			
			sb.append("<table style='border: solid gray 1px; border-collapse: collapse; width: 500px;'>");
			sb.append("<tr style='border: solid gray 1px; border-collapse: collapse;'>");
			sb.append("<td style='background-color: #DDDDDD; border: solid gray 1px; border-collapse: collapse;'>��ǰ��ȣ</td>");
			sb.append("<td>"+prodseq+"</td>");
			sb.append("</tr>");
			sb.append("<tr style='border: solid gray 1px; border-collapse: collapse;'>");
			sb.append("<td style='background-color: #DDDDDD; border: solid gray 1px; border-collapse: collapse;'>��ǰ��</td>");
			sb.append("<td>"+PRODNAME+"<br/>");
				for (int i=0; i<thumbnailfilenameArr.length; i++) {
			   	    sb.append(thumbnailfilenameArr[i]+"<br/>");
			   	 	sb.append("<img src=\"http://localhost:9090/board/resources/files/"+thumbnailfilenameArr[i]+"\"&nbsp;/>");
					
				 // sb.append("<img src=\"http://gdimg.gmarket.co.kr/707429960/still/120\"&nbsp;/>");		
			   	}
			sb.append("</td>");
			sb.append("</tr>");
			sb.append("<tr style='border: solid gray 1px; border-collapse: collapse;'>");
			sb.append("<td style='background-color: #DDDDDD; border: solid gray 1px; border-collapse: collapse;'>������</td>");
			sb.append("<td>"+PRODCOMPANY+"</td>");
			sb.append("</tr>");
			sb.append("<tr style='border: solid gray 1px; border-collapse: collapse;'>");
			sb.append("<td style='background-color: #DDDDDD; border: solid gray 1px; border-collapse: collapse;'>�԰�</td>");
			sb.append("<td>"+ibgoqty+"</td>");
			sb.append("</tr>");
			sb.append("</table>");
			
		   	sb.append("<br/>�԰� �Ǿ����ϴ�.");
		   	
		   	String emailContents = sb.toString();
		   	
		   	HttpSession session = req.getSession();
		   	String emailAddress = ((MemberVO)session.getAttribute("loginuser")).getEmail();
		   	
		   	mail.sendmail_IbgoFinish(emailAddress, emailContents);
		 // ======= ***** ��ǰ�԰� �Ϸ�Ǿ��ٶ�� email ������ �� ***** ======= //
		   	
		}
		else {
		   msg = "�԰� ����!!";
		   loc = "javascript:history.back();";
		}
		    		
		req.setAttribute("msg", msg);
		req.setAttribute("loc", loc);
			
		return "msg.notiles";
	}
	
	
	@RequestMapping(value="/product/listProduct.action", method={RequestMethod.GET} ) 
	public String listProduct(HttpServletRequest req) {
	
		List<HashMap<String, String>> productList = service.getListProduct();
		
		req.setAttribute("productList", productList);
		
		return "product/listProduct.tiles2";
	}
	
	
	@RequestMapping(value="/product/viewProduct.action", method={RequestMethod.GET} ) 
	public String viewProduct(HttpServletRequest req) {
	
		String fk_prodseq = req.getParameter("fk_prodseq");
		
		HashMap<String, String> viewProductmap = service.getviewProduct(fk_prodseq);
		req.setAttribute("viewProductmap", viewProductmap);
		
		List<HashMap<String, String>> viewProductImageList = service.getviewProductImage(fk_prodseq);
		req.setAttribute("viewProductImageList", viewProductImageList);
		
		return "product/view.tiles2";
	}
	
	
	// >>> Ajax(jackson ���̺귯���� ����� ��) 
    //     ����� �̹��� Ŭ���� spring_productimag ���̺� ����� ����ũ���� �̹��� �����̸���  
    //     ��ȸ�� �ͼ� JSON Ÿ������ �����Ͽ� �Ѱ��ִ� ��û �����ϱ�
    @RequestMapping(value="/product/getLargeImgFilename.action", method={RequestMethod.GET} ) 
    @ResponseBody
    public HashMap<String, String> getLargeImgFilename(HttpServletRequest req) {
		
		String fk_prodseq = req.getParameter("fk_prodseq");               // ��ǰ��ȣ(fk_prodseq)
		String thumbnailFileName = req.getParameter("thumbnailFileName"); // ��������ϸ�
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("fk_prodseq", fk_prodseq);
		map.put("thumbnailFileName", thumbnailFileName);
		
		
		String imgFilename = service.getLargeImgFilename(map); 
		// spring_productimage ���̺��� 
		// ��ǰ��ȣ(fk_prodseq), ��������ϸ�(thumbnailFileName)�� �ش��ϴ� imagefilename �÷��� ��(201706110056542344396781698764.jpg) �������� 
		
		HashMap<String, String> returnmap = new HashMap<String, String>(); 
		
		returnmap.put("IMGFILENAME", imgFilename);
		
		return returnmap;
		
	}// end of getLargeImgFilename(HttpServletRequest req)------------------
	
	
	
	
}









