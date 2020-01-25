package com.smkj.shiroAndJwt.service.impl;

import com.smkj.shiroAndJwt.dao.ArticleMapper;
import com.smkj.shiroAndJwt.entiry.Article;
import com.smkj.shiroAndJwt.service.ArticleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Override
    public int upload(Article article) {
        return articleMapper.upload(article);
    }

    @Override
    public int delete(Article article) {
        return articleMapper.delete(article);
    }

    @Override
    public int update(Article article) {
        return articleMapper.update(article);
    }

    @Override
    public List<Article> retrieve(String keyText) {
        return articleMapper.retrieve(keyText);
    }
}
