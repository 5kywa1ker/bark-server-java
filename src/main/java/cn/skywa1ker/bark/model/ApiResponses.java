package cn.skywa1ker.bark.model;

import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author hfb
 * @date 20/1/15
 * @param <T>
 */
@Data
@Accessors(chain = true)
@Builder
public class ApiResponses<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表示接口调用成功
     */
    public static final int SUCCESS = 200;
    /**
     * 表示接口调用失败
     */
    public static final int FAIL = 500;
    /**
     * 表示没有权限调用该接口
     */
    public static final int NO_PERMISSION = 403;

    public static final String SUCC_MSG = "成功";
    public static final String FAIL_MSG = "失败";

    private String message;
    private Integer code;
    /**
     * 结果集返回
     */
    private T data;

    /**
     * 返回成功
     *
     */
    public static ApiResponses<Void> success() {
        return ApiResponses.<Void>builder().code(SUCCESS).message(SUCC_MSG).build();

    }

    /**
     * 成功返回
     *
     * @param object
     */
    public static <T> ApiResponses<T> success(T object) {
        return ApiResponses.<T>builder().code(SUCCESS).message(SUCC_MSG).data(object).build();
    }

    /**
     * 返回失败
     *
     */
    public static ApiResponses<Void> failure() {
        return ApiResponses.<Void>builder().code(FAIL).message(FAIL_MSG).build();
    }

    /**
     * 返回失败
     *
     */
    public static ApiResponses<Void> failure(int result, String msg) {
        return ApiResponses.<Void>builder().code(result).message(msg).build();
    }

}
