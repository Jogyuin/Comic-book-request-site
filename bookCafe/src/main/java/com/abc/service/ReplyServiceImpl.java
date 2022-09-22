package com.abc.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.dao.ReplyDAO;
import com.abc.domain.Reply;
import com.abc.util.PageNavigator;

@Service
public class ReplyServiceImpl implements ReplyService{

	@Autowired
	private ReplyDAO rDAO;
	
	@Override
	public List<Reply> selectReplyByBoardNum(int boardNum, PageNavigator navi) {
		
		RowBounds rb = new RowBounds(navi.getStartRecord(), navi.getCountPerPage()); 
		
		return rDAO.selectReplyByBoardNum(boardNum, rb);
	}

	@Override
	public Reply selectOneReply(int replyNum) {
		return rDAO.selectOneReply(replyNum);
	}

	@Override
	public int insertReply(Reply reply) {
		return rDAO.insertReply(reply);
	}

	@Override
	public int deleteReply(int replyNum) {
		return rDAO.deleteReply(replyNum);
	}

	@Override
	public PageNavigator getPageNavigator(int boardNum, int pagePerGroup, int countPerPage, int page) {
		
		int total = rDAO.selectReplyByBoardNum(boardNum).size();
		
		PageNavigator navi = new PageNavigator(pagePerGroup, countPerPage, page, total);
		
		return navi;
	}
}
