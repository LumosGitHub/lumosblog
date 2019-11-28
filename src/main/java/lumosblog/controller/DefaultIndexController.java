package lumosblog.controller;

import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.io.FeedException;
import lombok.extern.slf4j.Slf4j;
import lumosblog.model.dto.Comment;
import lumosblog.model.dto.Content;
import lumosblog.model.entity.*;
import lumosblog.service.CommentsService;
import lumosblog.service.ContentsService;
import lumosblog.utils.LumosUtils;
import lumosblog.utils.RestReturns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import java.util.*;


/**
 * @author : 冠麟
 * @date : 2019/9/29 10:42
 * 首页 控制器
 */
@Slf4j
@Controller
public class DefaultIndexController {


    /**
     * 文章id
     */
    private int articleId = 0;


    /**
     * 评论id
     */
    private int commentNumber = 0;

    /**
     * 文章分页数
     */
    private int contentNumber = 0;

    /**
     * 搜索title 关键字
     */
    private String keyWord;

    /**
     * 搜索 标签关键字
     */
    private String keyTags;

    /**
     * 搜索 分类关键字
     */
    private String keyCategory;


    /**
     * 判断是搜索 标题 标签 分类  0 1 2
     */
    private int keySearch;

    /**
     * 自定义页面名称
     */
    private String customName;


    /**
     * 文章展示小图标，后续可与分类 绑定
     */
    private static final String[] ICONS = {"bg-ico-book", "bg-ico-game", "bg-ico-note", "bg-ico-chat", "bg-ico-code", "bg-ico-image", "bg-ico-web", "bg-ico-link", "bg-ico-design", "bg-ico-lock"};

    @Autowired
    private ContentsService contentsService;

    @Autowired
    private CommentsService commentsService;

    @GetMapping("")
    public String defaultPage(@RequestParam(value = "cp", defaultValue = "1") Integer cp) {
        contentNumber = cp - 1;
        return "/user/defaultIndex.html";
    }

    /**
     * 归档 页面
     *
     * @return
     */
    @GetMapping("/archives")
    public String archives() {
        return "/user/archives.html";
    }


    /**
     * 自定义页面
     *
     * @param pageName 自定义的页面
     * @return
     */
    @GetMapping("/c/{pageName}")
    public String customizePage(@PathVariable String pageName, @RequestParam(value = "cp", defaultValue = "1") Integer cp) {
        commentNumber = cp - 1;
        Contents contents = contentsService.getCustomize("publish", "page", pageName);
        if (contents == null) {
            return "/user/null.html";
        } else {
            customName = pageName;
            articleId = contents.getCid();
            return "/user/customize.html";
        }

    }


    /**
     * 搜索页面跳转
     *
     * @param value 搜索关键字
     * @return html
     */
    @GetMapping("/search/{value}")
    public String searchTitle(@PathVariable(required = false) String value) {
        keyWord = value;
        keySearch = 0;
        return "/user/search.html";
    }

    /**
     * 某个标签页面
     *
     * @param value 标签值
     * @return
     */
    @GetMapping("/tag/{value}")
    public String searchTags(@PathVariable String value) {
        keyTags = value;
        keySearch = 1;
        return "/user/search.html";
    }

    /**
     * 某个分类页面
     *
     * @param value 分类值
     * @return
     */
    @GetMapping("/category/{value}")
    public String searchCategory(@PathVariable String value) {
        keyCategory = value;
        keySearch = 2;
        return "/user/search.html";
    }


    /**
     * post 请求 无法获取静态文件
     * 跳转文章页
     *
     * @return
     */
    @RequestMapping("/article/{id}")
    public String post(@PathVariable Integer id, @RequestParam(value = "cp", defaultValue = "1") Integer cp) {
        commentNumber = cp - 1;
        articleId = id;
        return "/user/post.html";
    }


    /**
     * 获取文章内容
     *
     * @return
     */
    @GetMapping("/api/article/{id}")
    @ResponseBody
    public RestReturns article(@PathVariable Integer id) {
        articleId = id;
        Contents contents = contentsService.getContentsById(id);
        Content content = new Content(contents);
        content.setArrayCategories(contents.getCategories().split(","));
        content.setArrayTags(contents.getTags().split(","));
        return RestReturns.ok(content);
    }

    /**
     * 自定义页面 api
     *
     * @return
     */
    @GetMapping("/api/customize/{value}")
    @ResponseBody
    public RestReturns custom(@PathVariable String value) {

        customName = value;

        Contents contents = contentsService.getCustomize("publish", "page", customName);
        articleId = contents.getCid();
        return RestReturns.ok(contents);

    }

    /**
     * Return相应文章的评论
     *
     * @return
     */
    @GetMapping("/api/customize")
    @ResponseBody
    public RestReturns articleComment() {
        int limit = 6;
        org.springframework.data.domain.Page<Comments> commentsPage = commentsService.getCommentsParent(articleId, "approved", PageRequest.of(commentNumber, limit));
        Page<Comment> page = commentPage(commentsPage);
        return RestReturns.ok(page);
    }

    private Page<Comment> commentPage(org.springframework.data.domain.Page<Comments> commentsPage) {

        List<Comments> parentComments = commentsPage.getContent();
        List<Comment> rootList = new ArrayList<>();
        for (Comments comment : parentComments) {
            int coId = comment.getCoid();
            List<Comments> childComments = commentsService.getCommentsChild(articleId, coId, "approved");

            List<Comments> copyComments = new ArrayList<>(commentsService.getCommentsChild(articleId, coId, "approved"));

            if (childComments != null && !childComments.isEmpty()) {

                for (Comments childComment : childComments) {
                    List<Comments> childComments1 = commentsService.getCommentsChild(articleId, childComment.getCoid(), "approved");
                    childComments1.forEach(item -> item.setPrevAuthor(childComment.getAuthor()));
                    copyComments.addAll(childComments1);

                }
                Comment comments = new Comment(comment);
                comments.setChildren(copyComments);
                comments.setLevels(1);
                rootList.add(comments);

            } else {
                Comment comments = new Comment(comment);
                comments.setLevels(0);
                rootList.add(comments);
            }
        }
        Collections.reverse(rootList);
        Page<Comment> page = new Page<>(commentsPage);
        page.setRows(rootList);
        return page;

    }


    /**
     * 获取最新评论 与 最新文章
     *
     * @return List[] 数组 0 是最新评论 ， 1 是最新文章
     */
    @GetMapping("/api/newest")
    @ResponseBody
    public List[] newestComments() {
        List<Comments> commentsList = commentsService.getNewestComment10("approved");
        List<Contents> contentsList = contentsService.getNewestContents("publish");
        commentsList.forEach(item -> {
            item.setAgent("");
            item.setCreated(0);
            item.setIp("");
            item.setMail("");
            item.setStatus("");
        });

        contentsList.forEach(item -> {
            item.setContent("");
            item.setCategories("");
            item.setStatus("");
            item.setTags("");
            item.setCreated(0);
            item.setFmtType("");
            item.setCommentsNum(0);
        });

        return new List[]{commentsList, contentsList};
    }


    /**
     * 文章展示
     *
     * @return
     */
    @GetMapping("/api/showContentsDefault/{number}")
    @ResponseBody
    public Page<ShowArticle> showContentsDefault(@PathVariable(value = "number") Integer number) {
        if (number != null) {
            contentNumber = number - 1;
        }
        org.springframework.data.domain.Page<Contents> page = contentsService.getContentsByStatusPage("publish", "post", PageRequest.of(contentNumber, 12));
        List<Contents> contentsList = page.getContent();
        List<ShowArticle> showArticles = new ArrayList<>();
        for (Contents contents : contentsList) {
            ShowArticle showArticle = new ShowArticle();
            showArticle.setCid(contents.getCid());
            showArticle.setContent(LumosUtils.intro(contents.getContent(), 75));
            showArticle.setTitle(contents.getTitle());
            showArticle.setSort(contents.getCategories());
            showArticle.setIco("item-meta-ico " + ICONS[contents.getCid() % ICONS.length]);
            showArticle.setImage("background-image:url(/r/default/static/img/rand/" + (contents.getCid() % 20) + ".jpg);");
            showArticles.add(showArticle);
        }

        Page<ShowArticle> showArticlePage = new Page<>(page);
        showArticlePage.setRows(showArticles);
        showArticlePage.setSuccess(true);
        return showArticlePage;

    }


    /**
     * 归档 api
     *
     * @return
     */
    @RequestMapping("/api/archivesContents")
    @ResponseBody
    public List<Archive> archivesContents() {
        return contentsService.getArchivesCount();

    }

    /**
     * 搜索api
     *
     * @return List<ShowSearch> {@link ShowSearch}
     */
    @GetMapping("/api/searchApi/{value}")
    @ResponseBody
    public List<ShowSearch> searchApi(@PathVariable String value) {
        keyWord = value;
        String title = "";
        List<Contents> contentsList;
        List<ShowSearch> list = new ArrayList<>();
        switch (keySearch) {

            case 0:
                contentsList = contentsService.searchTitle("publish", "post", keyWord);
                title = "搜索：" + keyWord;
                break;
            case 1:
                contentsList = contentsService.searchTags("publish", "post", "%" + keyTags + "%");
                title = "标签：" + keyTags;
                break;
            case 2:
                contentsList = contentsService.searchCategories("publish", "post", "%" + keyCategory + "%");
                title = "分类：" + keyCategory;
                break;
            default:
                contentsList = new ArrayList<>();
                break;
        }

        for (Contents contents : contentsList) {
            ShowSearch showSearch = new ShowSearch();
            showSearch.setCid(contents.getCid());
            showSearch.setKeyWord(title);
            showSearch.setTitle(contents.getTitle());
            showSearch.setIcon("item-meta-ico " + ICONS[contents.getCid() % ICONS.length]);
            showSearch.setCreated(contents.getCreated());
            list.add(showSearch);
        }
        return list;
    }


    /**
     * feed 聚合数据
     *
     * @return RSS XML
     */
    @GetMapping(value = "/api/feed", produces = "application/xml;charset=UTF-8")
    @ResponseBody
    public String feed() {
        List<Contents> list = contentsService.getContentsByStatus("publish");
        String xml = "";
        try {
            xml = LumosUtils.getRssXml(list);
        } catch (FeedException e) {
            e.printStackTrace();
        }
        return xml;
    }


}
