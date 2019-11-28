package lumosblog.model.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 冠麟
 * @date 2019/9/26 8:31
 */
@Data
@Table(name = "t_attach")
@Entity
public class Attach {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fname;
    private String ftype;
    private String fkey;
    private Integer authorId;
    private Integer created;

}
