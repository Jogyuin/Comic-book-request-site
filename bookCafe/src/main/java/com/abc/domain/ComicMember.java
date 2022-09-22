package com.abc.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor//기본생성자 args - arguments
@AllArgsConstructor//명시적 생성자 모움
public class ComicMember implements UserDetails{

	private String memberId;	// 사용자 아이디
	private String memberPw;	// 사용자 비밀번호
	private String memberName;	// 사용자 이름
	private String nickname;	// 사용자 닉네임
	private int age;	// 나이
	private String phone;	// 전화번호
	
	//Spring Security를 위한 필드
	private boolean enabled;	//계정 상태 1: ok 0: no
	private String roleName;	//사용저구문, ROLE_USER,ROLE_ADMIN

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getUsername() {
		return this.memberId;
	}

	@Override
	public String getPassword() {
		return this.memberPw;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() { //제대로 상속받은 값인지 확인을 위해서
		return this.enabled;
	}
}
