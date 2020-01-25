package com.smkj.shiroAndJwt.service;

import com.smkj.shiroAndJwt.entiry.Article;

import java.util.List;

public interface ArticleService {

    public int upload(Article article);

    public int delete(Article article);

    public int update(Article article);

    public List<Article> retrieve(String keyText);
}
