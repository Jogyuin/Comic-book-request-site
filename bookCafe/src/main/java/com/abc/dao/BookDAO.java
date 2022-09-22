package com.abc.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.abc.domain.Book;

@Mapper
public interface BookDAO {

		public List<Book> selectAllBook(Map<String, String> map, RowBounds rb); // 책 불러오기
		public int countBook(Map<String, String> map); // 책 세기
		
		//책 추가
		public int insertBook(Book book);
		//책수정하기
		public int updateBook(Book book);
		//책삭제하기
		public int deleteBook(int bookNum);
		//책하나만 선택
		public Book selectOneBook(int bookNum);
	
}
