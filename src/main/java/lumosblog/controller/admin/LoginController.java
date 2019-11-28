package lumosblog.controller.admin;

import lombok.extern.slf4j.Slf4j;
import lumosblog.model.params.LoginParam;
import lumosblog.utils.RestReturns;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author 冠麟
 * @date 2019/9/28 16:38
 * 登录控制
 */
@Slf4j
@Controller
public class LoginController {


    @GetMapping("/LumosLogin")
    public String login() {
        log.info("Login.html");
        return "/admin/login.html";
    }


    @PostMapping("/LumosLogin/login")
    @ResponseBody
    public RestReturns doLogin(LoginParam loginParam) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginParam.getUsername(), loginParam.getPassword());

        try {
            subject.login(token);
        } catch (LockedAccountException lae) {
            log.info("账户已锁定");
            return RestReturns.fail("账户已锁定!");
        } catch (ExcessiveAttemptsException eae) {
            log.info("用户名或密码错误次数过多!");
            return RestReturns.fail("用户名或密码错误次数过多!");
        } catch (AuthenticationException ae) {
            log.info("用户名或密码不正确！");
            return RestReturns.fail("用户名或密码不正确！");
        }
        if (subject.isAuthenticated()) {
            log.info("登录成功！");
            return RestReturns.ok();
        } else {
            token.clear();
            log.info("登录失败！");
            return RestReturns.fail("登录失败！");
        }


    }


}
