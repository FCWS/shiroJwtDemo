package com.smkj.shiroAndJwt.controller;

import com.alibaba.fastjson.JSONObject;
import com.smkj.shiroAndJwt.bean.ResponseBean;
import com.smkj.shiroAndJwt.entiry.Article;
import com.smkj.shiroAndJwt.entiry.ArticlePoll;
import com.smkj.shiroAndJwt.service.ArticleService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    /**
     * 文章上传接口，需要登陆用户才能访问
     * @param article 上传文章
     * @param request
     * @param response
     * @return 上传状态
     */
    @RequiresAuthentication
    @RequiresRoles(logical = Logical.OR, value = {"admin", "normal"})
    @PostMapping("/upload")
    public ResponseBean upload(@RequestBody Article article, HttpServletRequest request, HttpServletResponse response){
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            article.setUpdateTime(df.format(new Date()));
            article.setUploadTime(df.format(new Date()));
            int upload = articleService.upload(article);
            if (upload > 0) {
                return new ResponseBean(200, "上传成功，等待审核", article);
            } else {
                return new ResponseBean(200, "上传失败", null);
            }
        } catch (Exception e) {
            return new ResponseBean(200, "上传失败，请联系管理员", e.getMessage());
        }
    }

    @RequiresAuthentication
    @RequiresRoles(logical = Logical.OR, value = {"admin", "normal"})
    @PostMapping("/delete")
    public ResponseBean delete(@RequestBody Article article, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (article == null)
                return new ResponseBean(200, "参数错误", article);
            int delete = articleService.delete(article);
            if (delete > 0)
                return new ResponseBean(200, "删除成功", article);
            else
                return new ResponseBean(200, "删除失败", article);
        } catch (Exception e) {
            return new ResponseBean(200, "删除文章失败, 请联系管理员", e.getMessage());
        }
    }

    @RequiresRoles(logical = Logical.OR, value = {"admin", "normal"})
    @PostMapping("/update")
    public ResponseBean update(@RequestBody Article article, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (article == null)
                return new ResponseBean(200, "参数错误", article);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            article.setUpdateTime(df.format(new Date()));
            int count  = articleService.update(article);
            if (count > 0)
                return new ResponseBean(200, "更新成功", article);
            else
                return new ResponseBean(200, "更新失败", article);
        } catch (Exception e) {
            return new ResponseBean(200, "更新文章失败, 请联系管理员", e.getMessage());
        }
    }

    @RequiresAuthentication
    @PostMapping("/retrieve")
    public ResponseBean retrieve(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        try {
            JSONObject dataObject = JSONObject.parseObject(data);
            String keyText = dataObject.getString("key");
            if (keyText == null)
                return new ResponseBean(200, "参数错误", null);
            List<Article> articles = articleService.retrieve(keyText);
            if (articles != null)
                return new ResponseBean(200, "查找文章成功", articles);
            else
                return new ResponseBean(200, "查找文章失败", null);
        } catch (Exception e) {
            return new ResponseBean(200, "查找文章失败, 请联系管理员", e.getMessage());
        }
    }

    /**
     * 登陆用户获取文章数据 包含是否点赞信息
     * @param data
     * @param request
     * @param response
     * @return
     */
    @RequiresAuthentication
    @PostMapping("/getAllAuth")
    public ResponseBean getAllAuth(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Article> articles = articleService.getAllAuth();
            if (articles != null)
                return new ResponseBean(200, "获取文章成功", articles);
            else
                return new ResponseBean(200, "获取文章失败", null);
        } catch (Exception e) {
            return new ResponseBean(200, "获取文章失败, 请联系管理员", e.getMessage());
        }
    }

    /**
     * 获取所有文章
     * @param data
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/getAll")
    public ResponseBean getAll(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Article> articles = articleService.getAll();
            if (articles != null)
                return new ResponseBean(200, "获取文章成功", articles);
            else
                return new ResponseBean(200, "获取文章失败", null);
        } catch (Exception e) {
            return new ResponseBean(200, "获取文章失败, 请联系管理员", e.getMessage());
        }
    }

    /**
     * 点赞
     * @param data
     * @param request
     * @param response
     * @return
     */
    @RequiresAuthentication
    @PostMapping("/support")
    public ResponseBean support(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        try {
            JSONObject dataObject = JSONObject.parseObject(data);
            int userId = dataObject.getInteger("userId");
            int articleId = dataObject.getInteger("articleId");
            int count = 0;
            ArticlePoll articlePoll = articleService.isSupport(userId, articleId);
            System.out.println("articlePoll：" + articlePoll);
            if (articlePoll != null) {
                count = articleService.cancelSupport(userId, articleId);
            } else {
                count = articleService.support(userId, articleId);
            }
            if (count < 0)
                return new ResponseBean(200, "操作失败", null);
            return new ResponseBean(200, "操作成功", null);
        } catch (Exception e) {
            return new ResponseBean(200, "点赞动作失败，请联系管理员", e.getMessage());
        }
    }

    /**
     * 浏览上报  对应文章浏览总数+1
     * @param data
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/browser")
    public ResponseBean browser(@RequestBody String data, HttpServletRequest request, HttpServletResponse response){
        try {
            int articleId = JSONObject.parseObject(data).getInteger("articleId");
            int count = articleService.browser(articleId);
            if (count > 0) {
                return new ResponseBean(200, "点赞成功", null);
            } else {
                return new ResponseBean(200, "点赞失败", null);
            }
        } catch (Exception e) {
            return new ResponseBean(200, "点赞失败", e.getMessage());
        }
    }


//    private void checkArticle(Article article) {
//
//    }
}
