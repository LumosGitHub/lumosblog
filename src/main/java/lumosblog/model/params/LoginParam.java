package lumosblog.model.params;

import lombok.Data;

/**
 * @author 冠麟
 * @date 2019/11/6 10:41
 */
@Data
public class LoginParam {

    private String username;
    private String password;
    private String rememberMe;

}
