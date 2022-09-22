package com.abc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.abc.dao.ComicMemberDAO;
import com.abc.domain.ComicMember;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class ComicMemberServiceImpl implements ComicMemberService {

	@Autowired
	private ComicMemberDAO cDAO;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public int insertMember(ComicMember member) {
		log.debug("insertmember실행");
		log.debug("member : {}",member);
	
		if(member.getNickname() == null || member.getNickname().trim().length() == 0) {
			String name = member.getMemberName();
			member.setNickname(name);
		}
			
		String encodedPassword = passwordEncoder.encode(member.getPassword());
		
		//암호화된 비번을 다시 user 객체에 설정
		member.setMemberPw(encodedPassword);
			
		return cDAO.insertMember(member);
	}

	@Override
	public ComicMember selectOneMember(String id) {
		log.debug("selectOnemember실행");
		log.debug("id : {}",id);
		
		return cDAO.selectOneMember(id);
	}

	@Override
	public int updateMember(ComicMember member) {
		String pw =	member.getMemberPw();
		String encodePw = passwordEncoder.encode(pw);
		
		member.setMemberPw(encodePw);
		
		return cDAO.updateMember(member);
	}

	@Override
	public int blockAccount(ComicMember member) {
		return cDAO.blockAccount(member);
	}
}
