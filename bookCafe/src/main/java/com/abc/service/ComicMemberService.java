package com.abc.service;

import com.abc.domain.ComicMember;

public interface ComicMemberService {
	public int insertMember(ComicMember member);	// 회원 가입
	public ComicMember selectOneMember(String id);	// 회원 찾기
	
	public int updateMember(ComicMember member);	// 회원 수정
	public int blockAccount(ComicMember member);	// 회원 삭제
}
