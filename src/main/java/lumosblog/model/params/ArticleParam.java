package lumosblog.model.params;

import lombok.Data;
import lombok.ToString;


/**
 * @author 10731
 */
@Data
@ToString(callSuper = true)
public class ArticleParam extends PageParam {

    /**
     * 搜索标题
     */
    private String title;

    /**
     * 分类
     */
    private String categories;

    /**
     * 状态
     */
    private String status;

    /**
     * 类型
     */
    private String type;

    /**
     *
     */
    private String orderBy;

}
