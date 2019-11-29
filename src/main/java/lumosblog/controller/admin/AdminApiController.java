package lumosblog.controller.admin;

import com.rometools.rome.feed.rss.Item;
import lombok.extern.slf4j.Slf4j;
import lumosblog.model.dto.Comment;
import lumosblog.model.entity.*;
import lumosblog.model.params.ArticleParam;
import lumosblog.model.params.MetaParam;
import lumosblog.model.params.PageParam;
import lumosblog.service.*;
import lumosblog.utils.LumosUtils;
import lumosblog.utils.RestReturns;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.Soundbank;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author 冠麟
 * @date 2019/10/5 11:58
 */
@Slf4j
@RestController
@RequestMapping("/admin/api/")
public class AdminApiController {


    /**
     * 评论
     */
    @Autowired
    CommentsService commentsService;

    /**
     * 文章与页面
     */
    @Autowired
    ContentsService contentsService;

    /**
     * 附件
     */
    @Autowired
    AttachService attachService;

    /**
     * 分类与标签
     */
    @Autowired
    MetasService metasService;


    @Autowired
    UsersService usersService;


    /**
     * ·评论管理·
     * 展示评论
     *
     * @param pageParam 分页参数
     * @return
     */
    @GetMapping("/comments")
    @ResponseBody
    public RestReturns getComments(PageParam pageParam) {

        org.springframework.data.domain.Page<Comments> comments = commentsService.getAllPage(PageRequest.of(pageParam.getPage() - 1, pageParam.getLimit()));
        Page<Comments> page = new Page<>(comments);
        List<Comments> list = new ArrayList<>();
        comments.forEach(list::add);
        page.setRows(list);
        return RestReturns.ok(page);
    }


    /**
     * ·页面管理·
     * 展示
     *
     * @param articleParam 分页参数
     * @return
     */
    @GetMapping("/pages")
    @ResponseBody
    public RestReturns pageList(ArticleParam articleParam) {

        org.springframework.data.domain.Page<Contents> contents = contentsService.getContentsByType("page", PageRequest.of(articleParam.getPage() - 1, articleParam.getLimit()));
        return getRestReturns(contents);
    }

    /**
     * 用于文章与页面展示数据
     *
     * @param contents 展示对象
     * @return
     */
    private RestReturns getRestReturns(org.springframework.data.domain.Page<Contents> contents) {
        Page<Contents> articles = new Page<>(contents);
        List<Contents> list = new ArrayList<>();
        contents.forEach(item -> {
            item.setContent("");
            list.add(item);
        });
        articles.setRows(list);
        return RestReturns.ok(articles);
    }


    /**
     * ·页面管理·
     * 删除页面
     *
     * @param cid 页面id
     * @return
     */
    @PostMapping("page/delete/{cid}")
    @ResponseBody
    public RestReturns deletePage(@PathVariable Integer cid) {
        contentsService.deleteContent(cid);
        log.info("删除页面 ID：" + cid);
        return RestReturns.ok();
    }

    /**
     * 新建页面
     *
     * @param contents 页面对象
     * @return
     */
    @PostMapping("/page/new")
    @ResponseBody
    public RestReturns newPage(@RequestBody Contents contents) {
        contents.setType("page");
        int cid = contentsService.save(contents).getCid();
        log.info("新建页面 ID：" + cid);
        return RestReturns.ok(cid);
    }

    /**
     * 更新页面
     *
     * @param contents 页面对象
     * @return
     */
    @PostMapping("/page/update")
    @ResponseBody
    public RestReturns updatePage(@RequestBody Contents contents) {
        contents.setType("page");
        log.info(contents.toString());
        int cid = contentsService.save(contents).getCid();
        log.info("更新页面 ID：" + cid);
        return RestReturns.ok(cid);
    }


    /**
     * ·发布文章·
     * 分类/标签
     *
     * @return
     */
    @GetMapping("/categories")
    @ResponseBody
    public RestReturns categoryList() {

        String type = "category";
        List<Metas> metas = metasService.getMetasByType(type);
        metas = metasService.getMetasCount(metas, type);

        return RestReturns.ok(metas);
    }

    /**
     * 标签
     *
     * @return
     */
    @GetMapping("tags")
    @ResponseBody
    public RestReturns tagList() {

        String type = "tag";
        List<Metas> metas = metasService.getMetasByType("tag");
        metas = metasService.getMetasCount(metas, type);
        return RestReturns.ok(metas);
    }

    /**
     * ·分类/标签管理·
     * 保存分类
     * <p>
     * <p>
     * id==null save - 直接保存
     * id=？ update - 遍历文章 - 查询分类 - 相同则修改
     *
     * @param metaParam 分类对象
     * @return
     */
    @PostMapping("category/save")
    @ResponseBody
    public RestReturns saveCategory(@RequestBody MetaParam metaParam) {

        if (metaParam.getMid() == null) {
            //保存
            Metas metas = new Metas();
            metas.setName(metaParam.getCname());
            metas.setMid(metaParam.getMid());
            metas.setType("category");
            metasService.saveMetas(metas);
        } else {
            //修改
            Metas metas = metasService.getMetasByIdAndType(metaParam.getMid(), "category");
            List<Contents> contentsList = metasService.modifyContentsCategories(metas.getName(), metaParam.getCname());
            contentsList.forEach(contentsService::save);
            Metas saveMetas = new Metas();
            saveMetas.setName(metaParam.getCname());
            saveMetas.setMid(metaParam.getMid());
            saveMetas.setType("category");
            metasService.saveMetas(saveMetas);

        }

        return RestReturns.ok();
    }

    /**
     * ·发布文章·
     * 删除分类/标签
     *
     * @param mid
     * @return
     */
    @PostMapping("category/delete/{mid}")
    @ResponseBody
    public RestReturns deleteMeta(@PathVariable Integer mid) {
        metasService.deleteMetas(mid);
        return RestReturns.ok();
    }


    /**
     * ·发布文章·
     * 保存新文章
     *
     * @param contents
     * @return
     */
    @PostMapping("article/new")
    @ResponseBody
    public RestReturns newArticle(@RequestBody Contents contents) {
        contents.setType("post");
        contents.setHits(1);
        int cid = contentsService.save(contents).getCid();
        log.info("保存文章 ID" + cid);
        return RestReturns.ok(cid);


    }

    /**
     * ·发布文章·
     * 更新文章
     *
     * @param contents
     * @return
     */
    @PostMapping("article/update")
    @ResponseBody
    public RestReturns updateArticle(@RequestBody Contents contents) {
        contents.setType("post");
        int cid = contentsService.save(contents).getCid();
        log.info("更新文章 ID：" + cid);
        return RestReturns.ok(cid);
    }


    /**
     * 回复评论
     *
     * @param request
     * @param comments
     * @return
     */
    @PostMapping("/comment/reply")
    @ResponseBody
    public RestReturns replyComment(HttpServletRequest request, @RequestBody Comments comments) {
        String usersName = (String) SecurityUtils.getSubject().getPrincipal();

        Comments c = commentsService.getCommentsById(comments.getCoid()).orElse(new Comments());

        if (c.getCid() == null) {
            return RestReturns.fail("不存在该评论");
        }

        Users users = usersService.getUsersByUsername(usersName);

        Comments saveComments = new Comments();

        if (comments.getMail() != null) {
            saveComments.setMail(comments.getMail());
        } else {
            saveComments.setMail("");
        }

        saveComments.setCid(c.getCid());
        saveComments.setOwnerId(c.getOwnerId());
        saveComments.setContent(comments.getContent());
        saveComments.setAuthor(usersName);
        saveComments.setAuthorId(users.getUid());
        saveComments.setIp(request.getRemoteAddr());
        saveComments.setCreated((int) (System.currentTimeMillis() / 1000));
        saveComments.setStatus("approved");
        saveComments.setParent(comments.getCoid());


        commentsService.save(saveComments);

        return RestReturns.ok();
    }

    /**
     * 删除评论
     *
     * @param coid 评论id
     * @return {@link RestReturns}
     */
    @PostMapping("/comment/delete/{coid}")
    @ResponseBody
    public RestReturns deleteComment(@PathVariable Integer coid) {
        commentsService.deleteComment(coid);
        log.info("删除评论 ID:" + coid);
        return RestReturns.ok();
    }


    /**
     * ·文章管理·
     * 编辑文章 获取文章 标题等
     *
     * @param cid 待编辑文章的id
     * @return {@link RestReturns}
     */
    @GetMapping("/articles/{cid}")
    @ResponseBody
    public RestReturns articleEdit(@PathVariable Integer cid) {
        Contents contents = contentsService.getContentsById(cid);
        contents.setContent("");
        return RestReturns.ok(contents);
    }

    /**
     * ·文章管理·
     * 编辑文章 获取文章内容
     *
     * @param cid 待编辑文章的id
     * @return {@link RestReturns}
     */
    @GetMapping("/articles/content/{cid}")
    @ResponseBody
    public RestReturns articleEditContent(@PathVariable Integer cid) {
        Contents contents = contentsService.getContentsById(cid);
        return RestReturns.ok(contents.getContent());
    }


    /**
     * ·文章管理·
     * 展示所有文章
     */
    @GetMapping("/articles")
    @ResponseBody
    public RestReturns articleList(ArticleParam articleParam) {

        org.springframework.data.domain.Page<Contents> contents = contentsService.getContentsByTitleLikeAndCategoriesLikeAndStatusLikeAndType(
                articleParam.getTitle(),
                articleParam.getCategories(),
                articleParam.getStatus(),
                articleParam.getType(),
                PageRequest.of(
                        articleParam.getPage() - 1,
                        articleParam.getLimit()));
        return getRestReturns(contents);
    }


    /**
     * 删除文章
     *
     * @param cid 文章id
     * @return {@link RestReturns}
     */
    @PostMapping("/article/delete/{cid}")
    @ResponseBody
    public RestReturns deleteArticle(@PathVariable Integer cid) {
        contentsService.deleteContent(cid);
        log.info("删除文章 ID：" + cid);
        return RestReturns.ok();
    }


    /**
     * 附件信息
     *
     * @param pageParam 分页参数
     * @return {@link RestReturns}
     */
    @GetMapping("/attaches")
    @ResponseBody
    public RestReturns attachList(PageParam pageParam) {

        org.springframework.data.domain.Page<Attach> attachPage = attachService.getAttachAll(PageRequest.of(pageParam.getPage() - 1, pageParam.getLimit()));
        Page<Attach> page = new Page<>(attachPage);
        List<Attach> list = new ArrayList<>();
        attachPage.forEach(list::add);
        page.setRows(list);

        return RestReturns.ok(page);
    }

    /**
     * 删除附件
     *
     * @param id 附件id
     * @return {@link RestReturns}
     */
    @PostMapping("attach/delete/{id}")
    @ResponseBody
    public RestReturns deleteAttach(@PathVariable Integer id) {
        Attach attach = new Attach();
        Optional<Attach> optionalAttach = attachService.getAttachById(id);
        if (optionalAttach.isPresent()) {
            attach = optionalAttach.get();
        }
        String filePath = LumosUtils.UP_DIR + "/upload" + attach.getFkey();
        File file = new File(filePath);
        boolean result = file.delete();
        if (!result) {
            log.error("本地附件删除失败！ 附件名： " + attach.getFkey());
        }
        attachService.deleteAttach(id);
        log.info("删除附件 ID：" + id + "  Name: " + attach.getFkey());
        return RestReturns.ok();
    }


}
