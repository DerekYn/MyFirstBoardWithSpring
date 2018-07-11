package com.spring.product.service;

import java.util.HashMap;
import java.util.List;

public interface InterProductService {

	List<HashMap<String, String>> getCatecode(); // ��ǰī�װ��ڵ�, ī�װ��� ��������
	
	List<HashMap<String, String>> getSpeccode(); // ��ǰ�����ڵ�, ��ǰ����� ��������
	
	int getProdseq(); // ������ �Է��� ��ǰ��ȣ(������) ��������
	
	int addProduct(HashMap<String, String> mapProduct); // ��ǰ�������̺� ��ǰ���� ����ϱ�
	
	int addProductimage(HashMap<String, String> mapProductimage); // ��ǰ�̹������̺� ��ǰ�̹������� ����ϱ�
	
	List<HashMap<String, String>> getProdseqList(); // ��ǰ��ȣ, ��ǰ�� ��������
	
	int addproductStore(HashMap<String, String> mapIbgo) throws Throwable; // ��ǰ�԰� ���̺� ��ǰ�԰��ϱ�
                                                                           // ��ǰ�������̺� �԰�� �縸ŭ ��� ������Ű��
	
	List<HashMap<String, String>> getListProduct(); // ��������ϸ��� ������ ��ǰ��� ��������
	
	HashMap<String, String> getviewProduct(String fk_prodseq); // Ư����ǰ�� ���� ��������
	List<HashMap<String, String>> getviewProductImage(String fk_prodseq); // Ư����ǰ�� �����̹������ϸ� �� ������̹������ϸ� �������� 
	
	String getLargeImgFilename(HashMap<String, String> map); // Ư�� ������̹������ϸ� ���� �����̹������ϸ� ��������  
	
}
