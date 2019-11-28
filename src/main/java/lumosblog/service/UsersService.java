package lumosblog.service;

import lumosblog.model.dao.UsersDao;
import lumosblog.model.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 冠麟
 * @date 2019/11/12 8:50
 */
@Service
public class UsersService {

    @Autowired
   private UsersDao dao;


   public Users getUsersByUsername(String username){
      return dao.findByUsername(username);
    }


}
