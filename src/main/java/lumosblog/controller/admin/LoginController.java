package lumosblog.controller.admin;

import lombok.extern.slf4j.Slf4j;
import lumosblog.model.params.LoginParam;
import lumosblog.utils.MapCache;
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

    /**
     * 登录缓存
     */
    private final static MapCache MAP_CACHE = MapCache.single();
    private final static int ERROR_COUNT = 5;


    @GetMapping("/LumosLogin")
    public String login() {
        log.info("Login.html");
        return "/admin/login.html";
    }

    @GetMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        log.info("退出登录");
        return "redirect:/";
    }

    /**
     * 登录验证
     *
     * @param loginParam 登录参数
     * @return {@link RestReturns}
     */
    @PostMapping("/LumosLogin/login")
    @ResponseBody
    public RestReturns doLogin(LoginParam loginParam) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginParam.getUsername(), loginParam.getPassword());

        if (MAP_CACHE.get(loginParam.getUsername()) == null) {
            MAP_CACHE.set(loginParam.getUsername(), 0);
        }
        if ((int) MAP_CACHE.get(loginParam.getUsername()) >= ERROR_COUNT) {
            return RestReturns.fail("用户名或密码错误次数过多! 24小时后在尝试");
        }
        try {
            subject.login(token);
        } catch (LockedAccountException lae) {
            log.info("账户已锁定");
            return RestReturns.fail("账户已锁定!");
        } catch (ExcessiveAttemptsException eae) {
            log.info("用户名或密码错误次数过多!");
            return RestReturns.fail("用户名或密码错误次数过多!");
        } catch (AuthenticationException ae) {
            MAP_CACHE.set(loginParam.getUsername(), (int) MAP_CACHE.get(loginParam.getUsername()) + 1, 86400);
            log.info("用户名或密码不正确！");
            return RestReturns.fail("用户名或密码不正确！");
        }
        if (subject.isAuthenticated()) {
            log.info("登录成功！");
            MAP_CACHE.clean();
            return RestReturns.ok();
        } else {
            token.clear();
            log.info("登录失败！");
            return RestReturns.fail("登录失败！");
        }


    }


}
