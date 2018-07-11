package com.spring.product.model;

import java.util.HashMap;
import java.util.List;

public interface InterProductDAO {

	List<HashMap<String, String>> getCatecode(); // ��ǰī�װ��ڵ�, ī�װ��� ��������
	
	List<HashMap<String, String>> getSpeccode(); // ��ǰ�����ڵ�, ��ǰ����� ��������
	
	int getProdseq(); // ������ �Է��� ��ǰ��ȣ(������) ��������
	
	int addProduct(HashMap<String, String> mapProduct); // ��ǰ�������̺� ��ǰ���� ����ϱ�
	
	int addProductimage(HashMap<String, String> mapProductimage); // ��ǰ�̹������̺� ��ǰ�̹������� ����ϱ�
	
	List<HashMap<String, String>> getProdseqList(); // ��ǰ��ȣ, ��ǰ�� ��������
	
	int insertProductibgo(HashMap<String, String> mapIbgo); // ��ǰ�԰� ���̺� ��ǰ�԰��ϱ�
	int updateProdqty(HashMap<String, String> mapIbgo); // ��ǰ�������̺� �԰�� �縸ŭ ��� ������Ű��
	
	List<HashMap<String, String>> getListProduct(); // ��������ϸ��� ������ ��ǰ��� ��������
	
	HashMap<String, String> getviewProduct(String fk_prodseq); // Ư����ǰ�� ���� ��������
	List<HashMap<String, String>> getviewProductImage(String fk_prodseq); // Ư����ǰ�� �����̹������ϸ� �� ������̹������ϸ� �������� 
	
	String getLargeImgFilename(HashMap<String, String> map); // Ư�� ������̹������ϸ� ���� �����̹������ϸ� ��������  
}




