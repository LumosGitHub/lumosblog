package lumosblog.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/** 统一异常处理
 * @author 冠麟
 * @date 2019/10/29 21:32
 */
//@ControllerAdvice
//@ResponseBody
public class GlobalExceptionHandler {


   /* @ExceptionHandler(Exception.class) //表示让Spring捕获到所有抛出的SignException异常，并交由这个被注解的方法处理。
    @ResponseStatus(HttpStatus.BAD_REQUEST)  //表示设置状态码。
    String handleException(){
        return "Exception Deal!";
    }*/


   @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ExceptionHandler(BindException.class)
    public Object MethodArgumentNotValidHandler() {
        System.out.println("11111111111111111111111111111111111111");
        return RestReturns.fail("不能这样的！");
    }





}
