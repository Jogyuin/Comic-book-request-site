package com.abc.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abc.domain.ComicMember;
import com.abc.service.ComicMemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("comicMember")
@Controller
public class ComicMemberController {
	@Autowired
	ComicMemberService cService;
	/**
	 * 회원가입 페이지를 띄우는 기능
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/join")
	public String join() {
		log.debug("join() 실행");
		return "comicMemberView/join";
	}

	/**
	 * 회원가입을 실행하는 기능
	 */
	@PostMapping("/join")
	public String join(ComicMember member) {
		log.debug("member의 정보 : {}", member);
		log.debug("member의 정보 : {}", member.getAge());

		int result = cService.insertMember(member);

		log.debug("결과 : {}", result);
		return "redirect:/";

	}

	/**
	 * ID 중복체크 창을 띄우는 기능
	 */
	@GetMapping("/idCheck")
	public String idCheck() {
		log.debug("idCheck() 실행");

		return "comicMemberView/idForm";
	}

	@PostMapping("/idCheck")
	public String idCheck(String searchId, Model model) {
		log.debug("searchId : {}", searchId);

		// DB에서 ID를 조회한 결과
		ComicMember searchedMember = cService.selectOneMember(searchId);
		boolean result = searchedMember == null ? true : false;

		log.debug("검색결과 {}", searchedMember);

		log.debug("id 사용가능여부 : {}", result);

		model.addAttribute("searchId", searchId);
		model.addAttribute("result", result);

		return "comicMemberView/idForm";
	}

	/**
	 * 로그인 폼을 띄우는 기능
	 */
	@GetMapping("/login")
	public String login(HttpServletRequest request) {
		String referrer = request.getHeader("Referer");
		request.getSession().setAttribute("prevPage", referrer);
		return "comicMemberView/loginForm";
	}
	
	@GetMapping("/checkLogin")
	public @ResponseBody boolean checkLogin(String memberId,String memberPw) {
      log.debug("아이디 {}",memberId);
      log.debug("비번 {}",memberPw);
      boolean result = false;
      
      ComicMember check = cService.selectOneMember(memberId);
      if(check!=null) {
         result = passwordEncoder.matches(memberPw, check.getMemberPw());
      }
      
      log.debug("결과 {}",result);
      
      return result;
	}
	
	// 회원정보 수정 화면 띄우기
	@GetMapping("/updateInfo")
	public String updateInfo(@AuthenticationPrincipal UserDetails user, Model model) {

		// 사용자식별자
		String memberId = user.getUsername();

		log.debug("memberID :{}", memberId);

		ComicMember member = cService.selectOneMember(memberId);

		model.addAttribute("member", member);
		return "comicMemberView/updateInfo";
	}

	// 회원 수정 기능
	@PostMapping("/update")
	public String update(ComicMember member, @AuthenticationPrincipal UserDetails user) {

		log.debug("member:{}", member);
		log.debug("username:{}", user.getUsername());
		member.setMemberId(user.getUsername());

		// service 객체호출
		cService.updateMember(member);

		return "redirect:/";
	}

	// 회원 삭제
	@GetMapping("/delete")
	public String delete(@AuthenticationPrincipal UserDetails user, Model model) {
		String memberId = user.getUsername();

		ComicMember member = cService.selectOneMember(memberId);

		model.addAttribute("member", member);
		return "comicMemberView/delete";
	}

	@PostMapping("/delete")
	public String delete(ComicMember member, HttpSession session, @AuthenticationPrincipal UserDetails user,
			HttpServletResponse response) throws IOException {
		member.setMemberId(user.getUsername());
		
		log.debug("memberID:{}", member.getMemberId());
		
		ComicMember checkMember = cService.selectOneMember(member.getMemberId());
		String memberPw = member.getMemberPw();
		
		log.debug("memberPW:{}", memberPw);

		String checkPw = checkMember.getMemberPw();
		
		log.debug("memberCheckPW:{}", checkPw);
		
		boolean check = passwordEncoder.matches(member.getMemberPw(), checkMember.getMemberPw());
		if (check != true) {
			
			log.debug("일치한거 없음 :{}", checkPw);
			
			response.setContentType("text/html; charset=UTF-8");
			
			PrintWriter out = response.getWriter();
			
			out.println("<script>alert('비밀번호를 잘못입력하셨습니다.'); location.href='/comicMember/delete';</script>");
			out.flush();
			
			return "redirect:/comicMember/delete";
		} else {
			member.setMemberPw(checkPw);
			cService.blockAccount(member);
			session.invalidate();
		}
		
		return "redirect:/";
	}
}
