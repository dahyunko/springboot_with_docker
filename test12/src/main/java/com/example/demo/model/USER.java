package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

//lombok 적용한 결과
@Entity(name="USER")
@Data
public class USER {
	@Id
	private String id;
	//@NonNull private String name; // null 값을 허용하지 않는다 
	//@Builder.Default //name 값이 null인 상태로 빌드가 되면 자동으로 "NULL NAME" 값이 삽입 
	@Column
	private String name;
	@Column
	private String password;	
}