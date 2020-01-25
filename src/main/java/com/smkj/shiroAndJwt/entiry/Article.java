package com.smkj.shiroAndJwt.entiry;

public class Article {
    private int id;
    private String author;
    private String title;
    private String context;
    private String intro;
    private String uploadTime;
    private String updateTime;
    private int isPublish; // 文章发布状态 ---> 1、管理员审核通过，已经发布；2、尚未发布
    private int publishMsg; // 审核意见

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(int isPublish) {
        this.isPublish = isPublish;
    }

    public int getPublishMsg() {
        return publishMsg;
    }

    public void setPublishMsg(int publishMsg) {
        this.publishMsg = publishMsg;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", context='" + context + '\'' +
                ", intro='" + intro + '\'' +
                ", uploadTime='" + uploadTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", isPublish=" + isPublish +
                ", publishMsg=" + publishMsg +
                '}';
    }
}
