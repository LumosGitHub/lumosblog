package lumosblog.shiro;

import lombok.extern.slf4j.Slf4j;
import lumosblog.model.entity.Users;
import lumosblog.service.UsersService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 冠麟
 * @date 2019/11/11 17:51
 */
@Slf4j
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    UsersService service;

    /**
     * 获取授权信息
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        log.info("查询授权用户：  "+username);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Users users= service.getUsersByUsername(username);
        Set<String> stringSet = new HashSet<>();
        stringSet.add(users.getRole());
        info.setStringPermissions(stringSet);
        return info;
    }


    /**
     * 获取身份验证信息
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String userName= (String) token.getPrincipal();
        String userPwd = new String((char[]) token.getCredentials());

        Users users= service.getUsersByUsername(userName);
        if (users==null){
            throw  new AuthenticationException("");
        }
        log.info("登录用户信息："+userName+"      "+userPwd);

        return new SimpleAuthenticationInfo(token.getPrincipal(),users.getPassword(),ByteSource.Util.bytes(userName+"guanlin"),getName());
    }



    public static String MD5pwd(String username, String pwd) {
        return     new SimpleHash("MD5", pwd, ByteSource.Util.bytes(username + "guanlin"), 2).toHex();

    }




}
