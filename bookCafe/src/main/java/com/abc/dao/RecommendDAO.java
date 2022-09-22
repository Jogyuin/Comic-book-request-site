package com.abc.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecommendDAO {
	public int insertRecommend(int boardNum); // 추천 추가
}
