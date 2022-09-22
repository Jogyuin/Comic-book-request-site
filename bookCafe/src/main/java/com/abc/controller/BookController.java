package com.abc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.abc.domain.Book;
import com.abc.service.BookService;
import com.abc.util.PageNavigator;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class BookController {
	
	@Autowired
	private BookService bService;
	
	@Value("${user.book.page}")
	private int countPerPage;
	
	@Value("${user.book.group}")
	private int pagePerGroup;
	
	//전체 책리스트
	@GetMapping({"/", "/bookList"})
	public String bookList(String type, String searchWord, 
							@RequestParam(name = "page", defaultValue = "1") int page,
							Model model) {
		
		log.debug("bookList() 실행");
		
		List<Book> bookList = null;
		PageNavigator navi = bService.getPageNavigator(pagePerGroup, countPerPage, page, type, searchWord);
				
		log.debug("type : {}, searchWord : {}", type, searchWord);
		
		if (searchWord == null || type == null) {
			//검색어와 카테고리 없으면 전체검색
			bookList = bService.selectAllBook(navi, null, null);
		} else {
			//검색어와 카테고리 있을 때 조건검색
			bookList = bService.selectAllBook(navi, type, searchWord);
			// 검색어와 타입을 model에 추가 
			model.addAttribute("type", type);
			model.addAttribute("searchWord", searchWord);
		}

		model.addAttribute("navi", navi);
		model.addAttribute("bookList", bookList);
		
		log.debug("bookList의 길이 : {}", bookList.size());
		
		return "bookView/bookList";
	}
	
	// 책 입고 화면 불러오기
	@GetMapping("/insertBook")
	public String insertBook() {
		log.debug("Get insertBook () 실행");
		
		return "bookView/insertBook";
	}
	
	// DB에 입고된 책 저장하기
	@PostMapping("/insertBook")
	public String insertBook(Book book, 
							@AuthenticationPrincipal UserDetails user) {
		log.debug("insertBook : {}", book);
		log.debug("insertBook user : {}", user);

		bService.insertBook(book);
		
		log.debug("book : {}", book);
		
		return "redirect:./";
	}
	
	//책 수정하기(화면호출)
	@GetMapping("/updateBook")
	public String updateBook(int num, Model model) {
		log.debug("updateBook() num : {}", num);
		
		//Service호출
		Book resultBook = bService.selectOneBook(num);
		
		log.debug("updateBook : {}", resultBook);
		
		model.addAttribute(resultBook);
		
		return "bookView/updateBook";
	}
	
	//책 수정하기(DB저장)
	@PostMapping("/updateBook")
	public String updateBook(Book book) {
		log.debug("update() book : {}", book);
		
		bService.updateBook(book);
		
		return "redirect:./";
	}
	
	// 책 삭제하기
	@GetMapping("deleteBook")
	public String deleteBook(int num) {
		log.debug("delete() num : {}", num);
		
		bService.deleteBook(num);
		
		return "redirect:./";
	}
	
}
