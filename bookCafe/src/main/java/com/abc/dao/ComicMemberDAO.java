package com.abc.dao;

import org.apache.ibatis.annotations.Mapper;

import com.abc.domain.ComicMember;

@Mapper
public interface ComicMemberDAO {
	
	public int insertMember(ComicMember member); // 회원 가입
	
	public ComicMember selectOneMember(String memberId); // 회원 불러오기

	public int updateMember(ComicMember member); // 회원 수정
	
	public int blockAccount(ComicMember member); // 회원 탈퇴
}
