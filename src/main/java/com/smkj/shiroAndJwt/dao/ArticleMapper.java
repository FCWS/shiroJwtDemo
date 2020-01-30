package com.smkj.shiroAndJwt.dao;

import com.smkj.shiroAndJwt.entiry.Article;
import com.smkj.shiroAndJwt.entiry.ArticlePoll;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper {
    public int upload(Article article);

    public int delete(Article article);

    public int update(Article article);

    public List<Article> retrieve(String keyText);

    public ArticlePoll isSupport(@Param("userId") int userId, @Param("articleId") int articleId);

    public int support(@Param("userId") int userId, @Param("articleId")int articleId);

    public int cancelSupport(@Param("userId") int userId, @Param("articleId")int articleId);

    public int browser(@Param("articleId") int articleId);

    public List<Article> getAllAuth();

    public List<Article> getAll();
}
