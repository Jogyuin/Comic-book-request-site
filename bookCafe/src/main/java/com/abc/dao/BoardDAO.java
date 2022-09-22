package com.abc.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.abc.domain.Board;
import com.abc.domain.Recommend;

@Mapper
public interface BoardDAO {
		
	public List<Board> selectAllBoard(Map<String, String> map, RowBounds rb); // 추천글 불러오기
	public int countBoard(Map<String, String> map); // 추천글 세기
	
	public int insertBoard(Board board); // 추천글 삽입
	public int updateBoard(Board board); // 추천글 수정
	
	public Board selectOneBoard(int boardNum); // 추천글 선택
	public int updateViewCount(int boardNum); // 추천글 조회수 늘리기
	
	public int deleteBoard(int boardNum); // 추천글 삭제
	
	public int insertRecommend(Recommend recommend); // DB에 추천 기록 추가
	public Recommend selectRecommend(Recommend recommend); // 추천 항목 검색
	public int updateRecommendCount(int boardNum); // 추천게시판 테이블에 추천수 늘리기
}
