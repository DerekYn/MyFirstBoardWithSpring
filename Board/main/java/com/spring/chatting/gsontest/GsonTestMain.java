package com.spring.chatting.gsontest;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

/*
	Member 클래스와 Family 클래스를 Gson 객체를 이용하여 JSON 표현식으로 변환했다가
	다시 자바 객체로 되돌리는 연습을 해보도록 한다.

*/


public class GsonTestMain {

	public static void main(String[] args) {

		List<Family> familyList1 = new ArrayList<Family>();

		familyList1.add(new Family("아버지","홍아빠",50));
		familyList1.add(new Family("어머니","길엄마",45));
		familyList1.add(new Family("형","홍형님",30));
		familyList1.add(new Family("동생","홍동생",22));
		
		Member member1 = new Member("홍길동", 26, familyList1);
	
		Gson gson = new Gson();
		String json_member1 = gson.toJson(member1);		// member1 객체를 json 타입으로 만든다.
		
		System.out.println(" ===== JSON 표현식으로 변환 ===== ");
		System.out.println(json_member1);
	
	//  ===== JSON 표현식으로 변환 ===== 	
	// {"name":"홍길동","age":26,"familys":[{"relation":"아버지","name":"홍아빠","age":50},{"relation":"어머니","name":"길엄마","age":45},{"relation":"형","name":"홍형님","age":30},{"relation":"동생","name":"홍동생","age":22}]}
		
		System.out.println("\n#########################################\n");
	
		String json_member2 = "{\"name\":\"이순신\",\"age\":27,\"familys\":[{\"relation\":\"아버지\",\"name\":\"이아빠\",\"age\":50},{\"relation\":\"어머니\",\"name\":\"순엄마\",\"age\":48},{\"relation\":\"동생\",\"name\":\"이동생\",\"age\":25}]}";
				
		System.out.println("===== JSON 표현식을 자바 객체로 복원 ======");
		
		Member mem1 = gson.fromJson(json_member1, Member.class);	// json형태로 되어진 것을 다시 객체로 만들어 주는 것	(바꿀객체,클래스명)
		Member mem2 = gson.fromJson(json_member2, Member.class);				
		
		System.out.println("1. 직원명 : "+mem1.getName());
		System.out.println("2. 직원나이 : "+mem1.getAge());
		System.out.println("3. 가족정보 : ");		
		for(Family fm :mem1.getFamilys()) {
			StringBuilder sb = new StringBuilder();	// 문자열 합쳐줄 때 사용
			sb.append("가족관계 => ");
			sb.append(fm.getRelation());	
			sb.append("		성명 => ");
			sb.append(fm.getName());
			sb.append("		나이 => ");
			sb.append(fm.getAge());
			String familyinfo = sb.toString();	// 문자열로 바꿔라
			
			System.out.println(familyinfo);
		}
		
		System.out.println(" -------------------------------------------- ");
		
		System.out.println("1. 직원명 : "+mem2.getName());
		System.out.println("2. 직원나이 : "+mem2.getAge());
		System.out.println("3. 가족정보 : ");		
		for(Family fm :mem2.getFamilys()) {
			StringBuilder sb = new StringBuilder();	// 문자열 합쳐줄 때 사용
			sb.append("가족관계 => ");
			sb.append(fm.getRelation());	
			sb.append("		성명 => ");
			sb.append(fm.getName());
			sb.append("		나이 => ");
			sb.append(fm.getAge());
			String familyinfo = sb.toString();	// 문자열로 바꿔라
			
			System.out.println(familyinfo);
		}
		
	}

}
