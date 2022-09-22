package com.abc.service;

import java.util.List;

import com.abc.domain.Reply;
import com.abc.util.PageNavigator;

public interface ReplyService {
	public List<Reply> selectReplyByBoardNum(int boardNum, PageNavigator navi);	// 댓글 불러오기(페이징 사용)
	
	public Reply selectOneReply(int replyNum);	// 댓글 불러오기
	
	public int insertReply(Reply reply);	// 댓글 입력
	
	public int deleteReply(int replyNum);	// 댓글 삭제
	
	public PageNavigator getPageNavigator(int boardNum, int pagePerGroup, int countPerPage, int page);
}
