package lumosblog.controller;

import lumosblog.model.entity.Comments;
import lumosblog.service.CommentsService;
import lumosblog.utils.MapCache;
import lumosblog.utils.RestReturns;
import lumosblog.utils.SettingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * @author 冠麟
 * @date 2019/10/20 18:08
 */
@Controller
public class ArticleController {


    @Autowired
    CommentsService service;

    private MapCache cache=MapCache.single();
    /**
     * 提交评论
     *  校验异常处理坑 待填  校验不符即抛异常，并不会执行后续方法。前端得到 400 error
     * @return
     */
    @PostMapping("/api/comment")
    @ResponseBody
    public RestReturns<?> comment(HttpServletRequest request, HttpServletResponse response, @Validated Comments comments, @RequestHeader String referer) {

        if (!referer.startsWith(SettingUtils.SOURCEIP)) {
            return RestReturns.fail("非法来源");
        }

        String val=request.getRemoteAddr()+":"+comments.getCid();
       Integer count= cache.hget("comments:frequency",val);

       if (count!=null&&count>0){
           return RestReturns.fail("您评论有点快哟！请稍后再试！");
       }

        cache.hset("comments:frequency", val, 1, 30);
        comments.setIp(request.getRemoteAddr());
        comments.setAgent(request.getHeader("User-Agent"));
        service.saveArticle(comments);

        try {
            Cookie cookie = new Cookie("tale_remember_author",  URLEncoder.encode(comments.getAuthor(), "UTF-8"));
            cookie.setPath("/");
            cookie.setMaxAge(604800);
            response.addCookie(cookie);

            Cookie cookie1 = new Cookie("tale_remember_mail", URLEncoder.encode(comments.getMail(), "UTF-8"));
            cookie1.setPath("/");
            cookie1.setMaxAge(604800);
            response.addCookie(cookie1);

            if (comments.getUrl() != null) {
            Cookie cookie2 = new Cookie("tale_remember_url", URLEncoder.encode(comments.getUrl(), "UTF-8"));
            cookie2.setPath("/");
            cookie2.setMaxAge(604800);
            response.addCookie(cookie2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return RestReturns.ok();
    }


}
