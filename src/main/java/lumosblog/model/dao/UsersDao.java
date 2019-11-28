package lumosblog.model.dao;

import lumosblog.model.entity.Contents;
import lumosblog.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 冠麟
 * @date 2019/11/12 8:49
 */
public interface UsersDao extends JpaRepository<Users, Integer> {

    /**
     * 根据 用户名查询数据
     * @param username 用户名
     * @return a {@link Users}
     */
    Users findByUsername(String username);

}
