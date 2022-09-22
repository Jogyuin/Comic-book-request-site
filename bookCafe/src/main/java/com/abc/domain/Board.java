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
public class Board {
	
	private int boardNum;	// 추천글 번호
	private String memberId;	// 회원 아이디
	private String nickname;	// 회원 닉네임
	private String bookName;	// 책 이름
	private String bookWriter;	// 책 작가
	private String title;	// 추천글 제목
	private String content;	// 추천글 내용
	private int viewCount;	// 조회수
	private String inputDate;	// 추천글 작성 날짜
	private int recommendCount;	// 추천수
	
	// 첨부파일용 필드
	private String originalFile;
	private String savedFile;

}
