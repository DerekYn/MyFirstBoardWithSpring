package com.spring.product.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.product.model.InterProductDAO;

@Service
public class ProductService implements InterProductService{

	@Autowired
	private InterProductDAO dao;

	
	// ��ǰī�װ��ڵ�, ī�װ��� ��������
	@Override
	public List<HashMap<String, String>> getCatecode() {
		List<HashMap<String, String>> catecodeList = dao.getCatecode();
		return catecodeList;
	}

	
	// ��ǰ�����ڵ�, ��ǰ����� ��������
	@Override
	public List<HashMap<String, String>> getSpeccode() {
		List<HashMap<String, String>> speccodeList = dao.getSpeccode();
		return speccodeList;
	}
	
	
	// ������ �Է��� ��ǰ��ȣ(������) ��������
	@Override
	public int getProdseq() {
		int prodseq = dao.getProdseq();
		return prodseq;
	}

	
	// ��ǰ�������̺� ��ǰ���� ����ϱ�
	@Override
	public int addProduct(HashMap<String, String> mapProduct) {
		int n = dao.addProduct(mapProduct);
		return n;
	}

	
	// ��ǰ�̹������̺� ��ǰ�̹������� ����ϱ�
	@Override
	public int addProductimage(HashMap<String, String> mapProductimage) {
		int n = dao.addProductimage(mapProductimage);
		return n;
	}
	
	
	// ��ǰ��ȣ, ��ǰ�� ��������
	@Override
	public List<HashMap<String, String>> getProdseqList() {
		List<HashMap<String, String>> prodseqList = dao.getProdseqList();
		return prodseqList;
	}
	

	// ��ǰ�԰� ���̺� ��ǰ�԰��ϱ�
    // ��ǰ�������̺� �԰�� �縸ŭ ��� ������Ű��
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation= Isolation.READ_COMMITTED, rollbackFor={Throwable.class})
	public int addproductStore(HashMap<String, String> mapIbgo)
			 throws Throwable{
		int n = dao.insertProductibgo(mapIbgo); 
		int m = dao.updateProdqty(mapIbgo);
		
		return n*m;
	}

	
	// ��������ϸ��� ������ ��ǰ��� ��������
	@Override
	public List<HashMap<String, String>> getListProduct() {
		List<HashMap<String, String>> productList = dao.getListProduct();
		return productList;
	}

	
	// Ư����ǰ�� ���� ��������
	@Override
	public HashMap<String, String> getviewProduct(String fk_prodseq) {
		HashMap<String, String> viewProductmap = dao.getviewProduct(fk_prodseq);
		return viewProductmap;
	}
	
	
	// Ư����ǰ�� �����̹������ϸ� �� ������̹������ϸ� �������� 
	@Override
	public List<HashMap<String, String>> getviewProductImage(String fk_prodseq) {
		List<HashMap<String, String>> viewProductImageList = dao.getviewProductImage(fk_prodseq);
		return viewProductImageList;
	}

	
	// Ư�� ������̹������ϸ� ���� �����̹������ϸ� ��������  
	@Override
	public String getLargeImgFilename(HashMap<String, String> map) {
		String imgFilename = dao.getLargeImgFilename(map);
		return imgFilename;
	}

	
	
	
}
