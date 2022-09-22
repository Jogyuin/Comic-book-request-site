package com.abc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Reply {
	private int replyNum;	// 댓글 번호
	private int boardNum;	// 추천글 번호
	private String memberId;	// 유저 아이디 
	private String nickname;	// 닉네임
	private String replyText;	// 댓글 내용
	private String inputDate;	// 댓글 입력날짜
}
