package com.emgc.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.ToString;

//R2DBC의 엔티티에는 Getter와 Setter만 필요
//엔티티명과 테이블명이 같다면 그대로, 다르다면 @Table을 통해 테이블명을 지정
@Data
@ToString
@Table("users")
public class User {

	//기본키에 해당하는 필드에 @Id애노테이션 필수
	@Id
	private Integer id;
	private String name;
	private Integer balance;

}
