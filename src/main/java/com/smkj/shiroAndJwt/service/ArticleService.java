package com.smkj.shiroAndJwt.service;

import com.smkj.shiroAndJwt.entiry.Article;
import com.smkj.shiroAndJwt.entiry.ArticlePoll;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleService {

    public int upload(Article article);

    public int delete(Article article);

    public int update(Article article);

    public List<Article> retrieve(String keyText);

    public ArticlePoll isSupport(int userId, int articleId);

    public int support(int userId, int articleId);

    public int cancelSupport(int userId, int articleId);

    public int browser(int articleId);

    public List<Article> getAllAuth();

    public List<Article> getAll();
}
