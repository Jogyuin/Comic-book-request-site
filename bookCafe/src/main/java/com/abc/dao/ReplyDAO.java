package com.abc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.abc.domain.Reply;

@Mapper
public interface ReplyDAO {
	public int insertReply(Reply reply); // 댓글 삽입
	public Reply selectOneReply(int replyNum);	// 댓글 불러오기
	public List<Reply> selectReplyByBoardNum(int boardNum, RowBounds rb);	// 댓글 페이징 불러오기
	public List<Reply> selectReplyByBoardNum(int boardNum);	// 댓글 전체 불러오기
	public int deleteReply(int replyNum);	// 댓글 삭제
}
