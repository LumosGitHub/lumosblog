package lumosblog.model.entity;

import lombok.Data;


import javax.persistence.*;

/**
 * @author 冠麟
 * @date 2019/11/12 8:30
 * 用户
 */
@Data
@Entity
@Table(name = "t_users")
public class Users {


    /**
     * 用户ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uid;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户权限
     */
    private String role;

}
