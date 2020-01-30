package com.smkj.shiroAndJwt.entiry;

public class ArticlePoll {
    private int id;
    private int userId;
    private int articleId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    @Override
    public String toString() {
        return "ArticlePoll{" +
                "id=" + id +
                ", userId=" + userId +
                ", articleId=" + articleId +
                '}';
    }
}
