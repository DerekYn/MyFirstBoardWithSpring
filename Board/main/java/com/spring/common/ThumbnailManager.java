package com.spring.common;

import java.io.File;
import java.util.Calendar;

import org.springframework.stereotype.Repository;

import net.coobird.thumbnailator.Thumbnails;

// ===== #166. ������� �ٷ���ִ� Ŭ���� �����ϱ� =====
@Repository
public class ThumbnailManager {

	public String doCreateThumbnail(String filename, String path) 
		throws Exception {
		
		String thumbnailFileName = null;
		
		// ���ε��� ������ �̸� ==> 2016100719592316420706146795.png
		if(filename.equals(""))
			return null;
		 
		
		// Ȯ����(.png)								 // lastIndexOf �Ǹ������������� .
		String fileExt = filename.substring(filename.lastIndexOf("."));
		// ���ڿ�.lastIndexOf("�˻���", �˻��� ������ ��ġ �ε���)
		// ���ڿ�.lastIndexOf("�˻���", 0)
		// ���ڿ�.lastIndexOf("�˻���") -- �˻��� ������ ��ġ �ε����� �����ϸ� �⺻������ 0 �� �ȴ�.
		// ==> ���ڿ����� Ž��(�˻�)�ϴ� ���ڿ��� ���������� �����ϴ� ��ġ�� ���� index�� ��ȯ.
		// 
		// ���ڿ�.indexOf("�˻���", �˻��� ������ ��ġ �ε���)��  
		// ���ڿ�.indexOf("�˻���", 0)
		// ���ڿ�.indexOf("�˻���") -- �˻��� ������ ��ġ �ε����� �����ϸ� �⺻������ 0 �� �ȴ�.
		// ==> ���ڿ����� Ž��(�˻�)�ϴ� ���ڿ��� ó�� �߰ߵǴ� ���ڿ��� ���� index�� ��ȯ.
		if(fileExt == null || fileExt.equals(""))
			return null;
		
		// ������ ������ ���ο� thumbnailFileName ���ϸ��� �����.
		thumbnailFileName = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", 
				                          Calendar.getInstance());
		thumbnailFileName += System.nanoTime();
		thumbnailFileName += fileExt;
		
		// ���ε��� ��ΰ� �������� �ʴ� ��� ������ ���� �Ѵ�.
		File dir = new File(path);
		if(!dir.exists())
			dir.mkdirs();
		
		String pathFilename = path + File.separator + filename;
		String pathThumbnailFileName = path + File.separator + thumbnailFileName;
		
		File image = new File(pathFilename);
		File thumbnail = new File(pathThumbnailFileName);
		
		if(image.exists()) {
		    Thumbnails.of(image).size(100, 100).outputFormat(fileExt.substring(1)).toFile(thumbnail); 
		}
		
		return thumbnailFileName;
	}
	
}
