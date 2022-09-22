package com.abc.service;

import java.util.List;

import com.abc.domain.Book;
import com.abc.util.PageNavigator;

public interface BookService {
	
	public List<Book> selectAllBook(PageNavigator navi, String type, String keyword);
	
	public PageNavigator getPageNavigator(int pagePerGroup, int countPage, int page, String type, String keyword);

	public int insertBook(Book book);	// 책 삽입
	public int updateBook(Book book);	// 책 수정
	public int deleteBook(int bookNum);	// 책 삭제
	public Book selectOneBook(int bookNum);	// 책 불러오기
}
