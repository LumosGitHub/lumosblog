package lumosblog.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 冠麟
 * @date 2019/9/17 18:58
 * 侧边栏统一跳转
 */
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {


    /**
     * 发布文章
     *
     * @return
     */
    @GetMapping("/article/new")
    public String articleNew() {
        return "/admin/article/new.html";
    }

    /**
     * 文章管理
     *
     * @return
     */
    @GetMapping("/articles")
    public String article() {
        return "/admin/articles.html";
    }


    /**
     * 页面管理
     *
     * @return
     */
    @GetMapping("/pages")
    public String articlePages() {
        return "/admin/pages.html";
    }






    /**
     * 文件管理
     *
     * @return
     */
    @GetMapping("/attaches")
    public String attaches() {
        return "/admin/attaches.html";
    }

    /**
     * 评论管理
     *
     * @return
     */
    @GetMapping("/comments")
    public String comments() {
        return "/admin/comments.html";
    }

    /**
     * 分类标签管理
     *
     * @return
     */
    @GetMapping("/categories")
    public String categories() {
        return "/admin/categories.html";
    }

    /**
     * 编辑模板（待删除）
     *
     * @return
     */
    @GetMapping("/template")
    public String template() {
        return "/admin/tpl_list.html";
    }

    @GetMapping("/themes")
    public String themes() {
        return "/admin/themes.html";
    }

    @GetMapping("/setting")
    public String setting() {
        return "/admin/setting.html";
    }

    /**
     * 编辑文章
     *
     * @param cid  文章id
     * @return
     */
    @GetMapping("/article/edit/{cid}")
    public String editArticle(@PathVariable Integer cid) {
        return "/admin/article/edit.html";
    }

    /**
     * 编辑页面
     * @param cid 页面id
     * @return
     */
    @GetMapping("/page/edit/{cid}")
    public String editPage(@PathVariable Integer cid){
        return "/admin/page/edit.html";
    }


    /**
     * 新建页面
     * @return
     */
    @GetMapping("/page/new")
    public String newPage(){
        return "/admin/page/new.html";
    }


}
