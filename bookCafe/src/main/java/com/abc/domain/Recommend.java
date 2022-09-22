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
public class Recommend {
	private int recommendNum;	// 추천 번호
	private String memberId;	// 추천한 사람 ID
	private int boardNum;	// 추천글 번호
}