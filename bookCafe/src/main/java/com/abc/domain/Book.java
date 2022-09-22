package com.abc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Book {
	
	private int bookNum;	// 책 번호
	private String bookName;	// 책 이름
	private String bookWriter;	// 책 작가
	private String genre;	// 책 장르
	private String bookLocation;	// 책 위치
	private String publisher;	// 출판사
	
}
