package lumosblog.model.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 冠麟
 * @date 2019/9/24 8:37
 * 分类与标签
 */
@Data
@Entity
@Table(name = "t_metas")
public class Metas {

    /**
     * 项目主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mid;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目缩略名
     */
    private String slug;

    /**
     * 项目类型
     */
    private String type;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 项目排序
     */
    private Integer sort = 0;

    /**
     * 父级
     */
    private Integer parent = 0;

    /**
     * 项目下文章数
     */

    private Integer count;


}
