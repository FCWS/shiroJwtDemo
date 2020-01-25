package com.smkj.shiroAndJwt.dao;

import com.smkj.shiroAndJwt.entiry.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper {
    public int upload(Article article);

    public int delete(Article article);

    public int update(Article article);

    public List<Article> retrieve(String keyText);
}
