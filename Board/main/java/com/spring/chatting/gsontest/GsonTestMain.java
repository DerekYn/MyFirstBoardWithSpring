package com.spring.chatting.gsontest;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

/*
	Member Ŭ������ Family Ŭ������ Gson ��ü�� �̿��Ͽ� JSON ǥ�������� ��ȯ�ߴٰ�
	�ٽ� �ڹ� ��ü�� �ǵ����� ������ �غ����� �Ѵ�.

*/


public class GsonTestMain {

	public static void main(String[] args) {

		List<Family> familyList1 = new ArrayList<Family>();

		familyList1.add(new Family("�ƹ���","ȫ�ƺ�",50));
		familyList1.add(new Family("��Ӵ�","�����",45));
		familyList1.add(new Family("��","ȫ����",30));
		familyList1.add(new Family("����","ȫ����",22));
		
		Member member1 = new Member("ȫ�浿", 26, familyList1);
	
		Gson gson = new Gson();
		String json_member1 = gson.toJson(member1);		// member1 ��ü�� json Ÿ������ �����.
		
		System.out.println(" ===== JSON ǥ�������� ��ȯ ===== ");
		System.out.println(json_member1);
	
	//  ===== JSON ǥ�������� ��ȯ ===== 	
	// {"name":"ȫ�浿","age":26,"familys":[{"relation":"�ƹ���","name":"ȫ�ƺ�","age":50},{"relation":"��Ӵ�","name":"�����","age":45},{"relation":"��","name":"ȫ����","age":30},{"relation":"����","name":"ȫ����","age":22}]}
		
		System.out.println("\n#########################################\n");
	
		String json_member2 = "{\"name\":\"�̼���\",\"age\":27,\"familys\":[{\"relation\":\"�ƹ���\",\"name\":\"�̾ƺ�\",\"age\":50},{\"relation\":\"��Ӵ�\",\"name\":\"������\",\"age\":48},{\"relation\":\"����\",\"name\":\"�̵���\",\"age\":25}]}";
				
		System.out.println("===== JSON ǥ������ �ڹ� ��ü�� ���� ======");
		
		Member mem1 = gson.fromJson(json_member1, Member.class);	// json���·� �Ǿ��� ���� �ٽ� ��ü�� ����� �ִ� ��	(�ٲܰ�ü,Ŭ������)
		Member mem2 = gson.fromJson(json_member2, Member.class);				
		
		System.out.println("1. ������ : "+mem1.getName());
		System.out.println("2. �������� : "+mem1.getAge());
		System.out.println("3. �������� : ");		
		for(Family fm :mem1.getFamilys()) {
			StringBuilder sb = new StringBuilder();	// ���ڿ� ������ �� ���
			sb.append("�������� => ");
			sb.append(fm.getRelation());	
			sb.append("		���� => ");
			sb.append(fm.getName());
			sb.append("		���� => ");
			sb.append(fm.getAge());
			String familyinfo = sb.toString();	// ���ڿ��� �ٲ��
			
			System.out.println(familyinfo);
		}
		
		System.out.println(" -------------------------------------------- ");
		
		System.out.println("1. ������ : "+mem2.getName());
		System.out.println("2. �������� : "+mem2.getAge());
		System.out.println("3. �������� : ");		
		for(Family fm :mem2.getFamilys()) {
			StringBuilder sb = new StringBuilder();	// ���ڿ� ������ �� ���
			sb.append("�������� => ");
			sb.append(fm.getRelation());	
			sb.append("		���� => ");
			sb.append(fm.getName());
			sb.append("		���� => ");
			sb.append(fm.getAge());
			String familyinfo = sb.toString();	// ���ڿ��� �ٲ��
			
			System.out.println(familyinfo);
		}
		
	}

}
