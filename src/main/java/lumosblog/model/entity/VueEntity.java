package lumosblog.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author 冠麟
 * @date 2019/9/15 15:00
 */
@Data
@AllArgsConstructor
public class VueEntity {

    private Integer id;
    private String username;
    private String password;


}
