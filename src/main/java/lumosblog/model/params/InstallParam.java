package lumosblog.model.params;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 冠麟
 * @date 2019/9/10 21:03
 */
@Getter
@Setter
@ToString
public class InstallParam {


    private String siteTitle;
    private String siteUrl;
    private String adminUser;
    private String adminEmail;
    private String adminPwd;


}
