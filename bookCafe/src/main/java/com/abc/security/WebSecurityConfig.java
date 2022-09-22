package com.abc.security;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.abc.util.CustomLoginSuccessHandler;

/**
 * 
 * 스프링 시큐리티 관련 설정을 하는 클래스
 *
 */
@Configuration //설정하는 클래스
public class WebSecurityConfig {
	
	@Autowired
	private DataSource dataSource;//어플리케이션 프로퍼티스에 있는 datasource 정보
	
	@Bean //스프링에서 사용할 수 있는 객체
	public SecurityFilterChain filterChain(HttpSecurity hs) throws Exception{
		hs.csrf().disable()	//csrf 인증방법, disable 무효
		.authorizeRequests()//권한 또는 인증요청
		.antMatchers(
				"/",
				"/bookList",
				"/board/list",
				"/board/read",
				"/board/display",
				"/comicMember/join",
				"/comicMember/checkLogin",
				"/comicMember/idCheck",
				"/images/**",
				"/css/**",
				"/js/**"
				)//get,PostMapping에 쓰는 주소값
		.permitAll() //모두허락, 인증없이 접근 가능
		.anyRequest().authenticated()	//위의 주소가 아닌 어떤 요청이라도 인증해야한다(로그인 되어있어야한다)
		.and()			//조건을 이어준다
		.formLogin()	//나는 재가 만든 폼으로 로그인한다
		.loginPage("/comicMember/login") //로그인을 여는 주소
		.loginProcessingUrl("/comicMember/login")	//로그인페이지에서 실행할 주소
		.successHandler(successHandler())
		.permitAll()
		.usernameParameter("memberId")	//사용자 정의 로그인폼에서 사용자 식별자로 사용하는 필드의 name 속성	
		.passwordParameter("memberPw")
		.and() //여기까지 로그인에 관한 설정
		.logout() //로그아웃에관한 설정
		.logoutSuccessUrl("/")		//로그아웃이 성공했을 때 이동할 주소
		.permitAll()
		.and()
		.cors()
		.and()
		.httpBasic();
		
		return hs.build(); // 위의 내용의 결과를 반환
	}
	
	@Bean
	public AuthenticationSuccessHandler successHandler() {
	    return new CustomLoginSuccessHandler("/defaultUrl");
	}
	
	//인증용 쿼리
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		String userNameQueryForEnabled =
				"SELECT MEMBERID username, MEMBERPW password, ENABLED " +
				"FROM COMICMEMBER " +
				"WHERE MEMBERID = ?";//? => memberId가 들어갈 곳

		String userNameQueryForRole = 
				"SELECT MEMBERID username, ROLENAME role_name " +
				"FROM COMICMEMBER " +
				"WHERE MEMBERID = ?";
		
		auth.jdbcAuthentication()
		.dataSource(dataSource)  //내가 db에 접속하기 위한 정보
		.usersByUsernameQuery(userNameQueryForEnabled) //Enabled 용 쿼리
		.authoritiesByUsernameQuery(userNameQueryForRole); //Role
	}
	
	// 단방향 비밀번호 암호화
	@Bean//스프링에서 사용하는 도구 객체
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
		//정적메서드 호출시 클래스명.이름 == static 객체생성전에 불러야해서 클래스 공간에 저장
		//스택 기본타입변수 참조타입변수의 주소값 아래에서 위로 쌓여있는것
		//힙은 무작위로 쌓임 참조객체
	}
}
