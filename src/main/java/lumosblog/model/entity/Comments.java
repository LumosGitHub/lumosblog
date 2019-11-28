package lumosblog.model.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author 冠麟
 * @date 2019/9/18 11:36
 * 评论
 */
@Data
@Entity
@Table(name = "t_comments")
public class Comments {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer coid;

    @NotNull
    private Integer cid;

    /**
     * 评论创建时间戳
     */
    private Integer created;

    /**
     * 创建者姓名
     */
    @NotNull
    @Length(max = 30,min = 1)
    private String author;
    private Integer authorId;
    private Integer ownerId;

    @Email
    @NotNull
    private String mail;
    private String url;
    private String ip;
    private String agent;

    @NotNull
    @Length(min = 5,max = 2000)
    private String content;

    private String type;
    private String status;
    private Integer parent;

    /**
     * 上一个作者
     * 用于《回复评论》 的 《回复评论》。。
     */
    @Transient
    private String prevAuthor;


}
