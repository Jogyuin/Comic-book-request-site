package com.abc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.dao.BookDAO;
import com.abc.domain.Book;
import com.abc.util.PageNavigator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookServiceImpl implements BookService{
	
	@Autowired
	private BookDAO bDAO;

	@Override
	public List<Book> selectAllBook(PageNavigator navi, String type, String keyword) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("type", type);	//검색카테고리
		map.put("searchWord", keyword);	//검색어
		
		log.debug("Service에서 type : {}, keyword : {}", type, keyword);

		//시작 레코드부터 한 페이지의 글 단위만큼 선택
		RowBounds rb = new RowBounds(navi.getStartRecord(), navi.getCountPerPage());
		
		return bDAO.selectAllBook(map, rb);
	}

	@Override
	public PageNavigator getPageNavigator(int pagePerGroup, int countPerPage, int page, String type, String keyword) {
		
		Map<String, String> map = new HashMap<String, String>();
	
		map.put("type", type);
		map.put("searchWord", keyword);
		
		int total = bDAO.countBook(map);
		
		PageNavigator navi = new PageNavigator(pagePerGroup, countPerPage, page, total);
		
		return navi;
	}

	@Override
	public int insertBook(Book book) {
		return bDAO.insertBook(book);
	}

	@Override
	public int deleteBook(int bookNum) {
		return bDAO.deleteBook(bookNum);
	}

	@Override
	public Book selectOneBook(int bookNum) {
		return bDAO.selectOneBook(bookNum);
	}

	@Override
	public int updateBook(Book book) {
		return bDAO.updateBook(book);
	}

}
