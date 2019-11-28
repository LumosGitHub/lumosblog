package lumosblog.model.entity;

import lombok.Data;

/**
 * @author 冠麟
 * @date 2019/9/29 10:34
 * 默认页文章展示
 */
@Data
public class ShowArticle {



    /**
     * 文章主要内容
     */
    String content;

    /**
     * 文章标题
     */
    String title;

    /**
     * 文章分类
     */
    String sort;

    /**
     * 文章小图标
     */
    String ico;


    /**
     * 文章背景图片
     */
    String image;


    Integer cid;


}
