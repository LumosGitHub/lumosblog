package lumosblog.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 冠麟
 * @date 2019/9/11 10:52
 */
@Data
public class Statistics implements Serializable {


    private static final long serialVersionUID = 2397406393624461886L;
    /**
     * 文章数
     */
    private long articles;

    /**
     * 页面数
     */
    private long pages;
    /**
     * 留言数
     */
    private long comments;
    /**
     * 分类数
     */

    private long categories;
    /**
     * 标签数
     */
    private long tags;
    /**
     * 附件数
     */
    private long attachs;


}
