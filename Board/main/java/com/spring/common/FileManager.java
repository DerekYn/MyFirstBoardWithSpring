package com.spring.common;

import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Repository;

// ===== #131. FileManager 클래스 생성하기 =====
@Repository
public class FileManager {

	// path : 파일을 저장할 경로
	// 리턴 : 서버에 저장된 새로운 파일명
	public String doFileUpload(byte[] bytes, String originalFilename, String path) throws Exception {	// 첨부된 파일을 읽어오는 메소드(byte타입으로)
		String newFilename = null;

		if(bytes == null)
			return null;
		
		// 클라이언트가 업로드한 파일의 이름
		if(originalFilename.equals(""))	
			return null;
		
		// 확장자												// lastIndexOf 맨마지막에나오는 . (강아지.jpg => .을 의미. 맨마지막에 붙은 .)
		String fileExt = originalFilename.substring(originalFilename.lastIndexOf(".")); 
		if(fileExt == null || fileExt.equals("")) // 확장자가 없으면
			return null;
		
		// 서버에 저장할 새로운 파일명을 만든다.
		newFilename = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", 		// 현재날짜 년월일시분초 
				         Calendar.getInstance());
		newFilename += System.nanoTime();									// += 나노타임
		newFilename += fileExt;												// 파일네임이 고유 할 수 밖에없다
		
		// 업로드할 경로가 존재하지 않는 경우 폴더를 생성 한다.
		File dir = new File(path);
		if(!dir.exists())	// 폴더가 존재하지 않으면
			dir.mkdirs();	// 만들어라
		
		String pathname = path + File.separator + newFilename;
		
		FileOutputStream fos = new FileOutputStream(pathname);
		fos.write(bytes);
		fos.close();
		
		return newFilename;	
	}

	public String doFileUpload(InputStream is, String originalFilename, String path) throws Exception {
		String newFilename = null;

		// 클라이언트가 업로드한 파일의 이름
		if(originalFilename==null||originalFilename.equals(""))
			return null;
		
		// 확장자
		String fileExt = originalFilename.substring(originalFilename.lastIndexOf("."));
		if(fileExt == null || fileExt.equals(""))
			return null;
		
		// 서버에 저장할 새로운 파일명을 만든다.
		newFilename = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", 
				         Calendar.getInstance());
		newFilename += System.nanoTime();
		newFilename += fileExt;
		
		// 업로드할 경로가 존재하지 않는 경우 폴더를 생성 한다.
		File dir = new File(path);
		if(!dir.exists())
			dir.mkdirs();
		
		String pathname = path + File.separator + newFilename;
		
		byte[] b=new byte[1024];
		int size=0;
		FileOutputStream fos = new FileOutputStream(pathname);
		
		while((size=is.read(b))!=-1) {
			fos.write(b, 0, size);
		}
		
		fos.close();
		is.close();
		
		return newFilename;
	}
	
	// 파일 다운로드
	// saveFilename : 서버에 저장된 파일명(숫자)
	// originalFilename : 클라이언트가 업로드한 파일명
	// path : 서버에 저장된 경로
	public boolean doFileDownload(String saveFilename, String originalFilename, String path, HttpServletResponse response) {
		String pathname = path + File.separator + saveFilename;
		
        try {
    		if(originalFilename == null || originalFilename.equals(""))
    			originalFilename = saveFilename;
        //	originalFilename = new String(originalFilename.getBytes("EUC-KR"),"8859_1"); 
    	//	또는
        	originalFilename = new String(originalFilename.getBytes("UTF-8"),"8859_1");
        } catch (UnsupportedEncodingException e) {
        }

	    try {
	        File file = new File(pathname);

	        if (file.exists()){
	            byte readByte[] = new byte[4096];

	            response.setContentType("application/octet-stream");
				response.setHeader(
						"Content-disposition",
						"attachment;filename=" + originalFilename);

				BufferedInputStream  fin = new BufferedInputStream(new FileInputStream(file));
				ServletOutputStream outs = response.getOutputStream(); 
	          // 또는 OutputStream outs = response.getOutputStream();
	          // ServletOutputStream 클라이언트로 이진 데이터를 보내주는 출력스트림이다. 
	          // 즉, ServletOutputStream 은 파일 다운로드에 사용되는 출력 스트림용 클래스이다. 
	            
	   			int length = 0;
	    		while ((length = fin.read(readByte, 0, 4096)) != -1) {
	    				outs.write(readByte, 0, length);
	    		}
	    		
	    		outs.flush();
	    		outs.close();
	            fin.close();
	            
	            return true;
	        }
	    } catch(Exception e) {
	    }
	    
	    return false;
	}
	
	// 실제 파일 삭제
	public void doFileDelete(String filename, String path) 
	        throws Exception {
		String pathname = path + File.separator + filename;
		File file = new File(pathname);
        if (file.exists())
           file.delete();
	}

	// 파일 길이
	public long getFilesize(String pathname) {
		long size=-1;
		
		File file = new File(pathname);
		if (! file.exists())
			return size;
		
		size=file.length();
		
		return size;
	}
	
	// 파일 타입
	public String getFiletype(String pathname) {
		String type="";
		try {
			URL u = new URL("file:"+pathname);
		    URLConnection uc = u.openConnection();
		    type = uc.getContentType();
		} catch (Exception e) {
		}
		
	    return type;
	}
	
	
	// 스마트에디터에서 사진첨부시 이미지의 크기를 구하기 위한 getImageWidth(), getImageHeight() 메소드를 아래와 같이 추가생성함.
	// 이미지 폭
	public int getImageWidth(String pathname) {
	   int width=-1;
		
	   File file = new File(pathname);
	     if (! file.exists())
		return width;
		
	   ParameterBlock pb=new ParameterBlock(); 
           pb.add(pathname); 
           RenderedOp rOp=JAI.create("fileload",pb); 

           BufferedImage bi=rOp.getAsBufferedImage(); 

           width = bi.getWidth(); 		
		
	   return width;
	}
		
	// 이미지 높이
	public int getImageHeight(String pathname) {
	   int height=-1;
		
	   File file = new File(pathname);
	     if (! file.exists())
		return height;
		
	   ParameterBlock pb=new ParameterBlock(); 
           pb.add(pathname); 
           RenderedOp rOp=JAI.create("fileload",pb); 

           BufferedImage bi=rOp.getAsBufferedImage(); 

           height = bi.getHeight();		
		
	   return height;
	}
	
}
