package com.spring.product.model;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDAO implements InterProductDAO {

	@Autowired
	private SqlSessionTemplate sqlsession;

	
	// ��ǰī�װ��ڵ�, ī�װ��� ��������
	@Override
	public List<HashMap<String, String>> getCatecode() {
		List<HashMap<String, String>> catecodeList = sqlsession.selectList("product.getCatecode");
		return catecodeList;
	}

	
	// ��ǰ�����ڵ�, ��ǰ����� ��������
	@Override
	public List<HashMap<String, String>> getSpeccode() {
		List<HashMap<String, String>> speccodeList = sqlsession.selectList("product.getSpeccode");
		return speccodeList;
	}
	
	
	// ������ �Է��� ��ǰ��ȣ(������) ��������
	@Override
	public int getProdseq() {
		int prodseq = sqlsession.selectOne("product.getProdseq");
		return prodseq;
	}

	
	// ��ǰ�������̺� ��ǰ���� ����ϱ�
	@Override
	public int addProduct(HashMap<String, String> mapProduct) {
		int n = sqlsession.insert("product.addProduct", mapProduct);
		return n;
	}

	
	// ��ǰ�̹������̺� ��ǰ�̹������� ����ϱ�
	@Override
	public int addProductimage(HashMap<String, String> mapProductimage) {
		int n = sqlsession.insert("product.addProductimage", mapProductimage);
		return n;
	}
	
	
	// ��ǰ��ȣ, ��ǰ�� ��������
	@Override
	public List<HashMap<String, String>> getProdseqList() {
		List<HashMap<String, String>> prodseqList = sqlsession.selectList("product.getProdseqList");
		return prodseqList;
	}

	
	// ��ǰ�԰� ���̺� ��ǰ�԰��ϱ�
	@Override
	public int insertProductibgo(HashMap<String, String> mapIbgo) {
		int n = sqlsession.insert("product.insertProductibgo", mapIbgo);
		return n;
	}
	
	
	// ��ǰ�������̺� �԰�� �縸ŭ ��� ������Ű��
	@Override
	public int updateProdqty(HashMap<String, String> mapIbgo) {
		int n = sqlsession.update("product.updateProdqty", mapIbgo);
		return n;
	}
	
	
	// ��������ϸ��� ������ ��ǰ��� ��������
	@Override
	public List<HashMap<String, String>> getListProduct() {
		List<HashMap<String, String>> productList = sqlsession.selectList("product.getListProduct");
		return productList;
	}

	
	// Ư����ǰ�� ���� ��������
	@Override
	public HashMap<String, String> getviewProduct(String fk_prodseq) {
		HashMap<String, String> viewProductmap = sqlsession.selectOne("product.getviewProduct", fk_prodseq);  
		return viewProductmap;
	}	
	
	
	// Ư����ǰ�� �����̹������ϸ� �� ������̹������ϸ� �������� 
	@Override
	public List<HashMap<String, String>> getviewProductImage(String fk_prodseq) {
		List<HashMap<String, String>> viewProductImageList = sqlsession.selectList("product.getviewProductImage", fk_prodseq);  
		return viewProductImageList;
	}

	
	// Ư�� ������̹������ϸ� ���� �����̹������ϸ� �������� 
	@Override
	public String getLargeImgFilename(HashMap<String, String> map) {
		String imgFilename = sqlsession.selectOne("product.getLargeImgFilename", map);
		return imgFilename;
	}

	

	

	

	
	
	
	
}
