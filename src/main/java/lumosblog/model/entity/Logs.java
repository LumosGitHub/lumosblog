package lumosblog.model.entity;

import lombok.Data;

/**
 * @author 冠麟
 * @date 2019/9/17 8:49
 * 登录日志
 */
@Data
public class Logs {
    private Integer id;
    private String action;
    private String Data;
    private Integer authorId;
    private String ip;
    private long created;
}
