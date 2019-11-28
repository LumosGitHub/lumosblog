package lumosblog.utils;

import lombok.Builder;
import lombok.Data;

/**
 * @author 冠麟
 * @date 2019/9/15 20:42
 */
@Data
public class RestReturns<T> {

    private T payload;

    private boolean success;

    private String msg;


    private int code = 0;

    private long timestamp;

    public RestReturns() {
        this.timestamp = DateKit.nowUnix();
    }

    public RestReturns(boolean success) {
        this.timestamp = DateKit.nowUnix();
        this.success = success;
    }

    public RestReturns(T payload, boolean success) {
        this.timestamp = DateKit.nowUnix();
        this.payload = payload;
        this.success = success;
    }


    public RestReturns<T> peek(Runnable runnable) {
        runnable.run();
        return this;
    }

    public RestReturns<T> success(boolean success) {
        this.success = success;
        return this;
    }

    public RestReturns<T> payload(T payload) {
        this.payload = payload;
        return this;
    }

    public RestReturns<T> code(int code) {
        this.code = code;
        return this;
    }

    public RestReturns<T> msg(String msg) {
        this.msg = msg;
        return this;
    }

    public static <T> RestReturns<T> ok() {
        return new RestReturns<T>().success(true);
    }

    public static <T> RestReturns<T> ok(T payload) {
        return new RestReturns<T>().success(true).payload(payload);
    }

    public static <T> RestReturns ok(T payload, int code) {
        return new RestReturns<T>().success(true).payload(payload).code(code);
    }

    public static <T> RestReturns<T> fail() {
        return new RestReturns<T>().success(false);
    }

    public static <T> RestReturns<T> fail(String msg) {
        return new RestReturns<T>().success(false).msg(msg);
    }

    public static <T> RestReturns fail(int code, String msg) {
        return new RestReturns<T>().success(false).msg(msg).code(code);
    }


}
