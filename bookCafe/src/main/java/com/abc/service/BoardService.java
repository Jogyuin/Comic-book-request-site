package com.abc.service;

import java.util.List;

import com.abc.domain.Board;
import com.abc.domain.Recommend;
import com.abc.util.PageNavigator;

public interface BoardService {
	// 추천글 목록 불러오기
	public List<Board> selectAllBoard(PageNavigator navi, String type, String keyword);
	
	// 페이징용 네비게이터
	public PageNavigator getPageNavigator(int pagePerGroup, int countPerPage, int page, String type, String keyword);
	
	public int insertBoard(Board board); // 추천글 삽입
	public int updateBoard(Board board); // 추천글 수정
	public int deleteBoard(int boardNum); // 추천글 삭제
	public Board selectOneBoard(int boardNum); // 추천글 하나 선택
	public int updateViewCount(int boardNum); // 조회수 늘리기
	
	public int insertRecommend(Recommend recommend); // 추천 항목 삽입
	public Recommend selectRecommend(Recommend recommend); // 추천글번호와 memberId가 일치하는 추천항목 검색
	public int updateRecommendCount(int boardNum); // 추천글 DB에 추천수 늘리기
}
