package lumosblog.model.entity;

import lombok.Data;

/** 用与搜索返回
 * @author 冠麟
 * @date 2019/11/1 17:55
 */
@Data
public class ShowSearch {

    /**
     * 搜索关键字
     */
    private String keyWord;
    /**
     * 标题
     */
    private String title;
    /**
     * 图标
     */
    private String icon;
    /**
     * 发布时间
     */
    private Integer created;
    /**
     * 文章id
     */
    private Integer cid;


}
