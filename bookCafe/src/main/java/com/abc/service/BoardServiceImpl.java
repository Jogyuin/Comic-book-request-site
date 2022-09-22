package com.abc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.dao.BoardDAO;
import com.abc.domain.Board;
import com.abc.domain.Recommend;
import com.abc.util.PageNavigator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	private BoardDAO bDAO;

	@Override
	public List<Board> selectAllBoard(PageNavigator navi, String type, String keyword) {
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", type);
		map.put("searchWord", keyword);
		
		RowBounds rb = new RowBounds(navi.getStartRecord(), navi.getCountPerPage());
		
		log.debug("type : {}, Keyword : {}", type, keyword);
		
		return bDAO.selectAllBoard(map, rb);
	}

	@Override
	public PageNavigator getPageNavigator(int pagePerGroup, int countPerPage, int page, String type, String keyword) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("type", type);
		map.put("searchWord", keyword);
		
		int total = bDAO.countBoard(map);
		
		PageNavigator navi = new PageNavigator(pagePerGroup, countPerPage, page, total);
		
		return navi;
	}

	@Override
	public int insertBoard(Board board) { // 추천글 삽입
		return bDAO.insertBoard(board);
	}

	@Override
	public int updateBoard(Board board) { // 추천글 수정
		return bDAO.updateBoard(board);
	}

	@Override
	public int deleteBoard(int boardNum) {  // 추천글 삭제
		return bDAO.deleteBoard(boardNum);
	}

	@Override
	public Board selectOneBoard(int boardNum) { // 추천글 하나 선택
		return bDAO.selectOneBoard(boardNum);
	}
	
	@Override
	public int updateViewCount(int boardNum) { // 조회수 늘리기
		return bDAO.updateViewCount(boardNum);
	}

	@Override
	public int insertRecommend(Recommend recommend) { // 추천 항목 삽입
		return bDAO.insertRecommend(recommend);
	}

	@Override
	public Recommend selectRecommend(Recommend recommend) { // 추천글번호와 memberId가 일치하는 추천항목 검색
		return bDAO.selectRecommend(recommend);
	}

	@Override
	public int updateRecommendCount(int boardNum) { // 추천글 DB에 추천수 늘리기
		return bDAO.updateRecommendCount(boardNum);
	}
}
