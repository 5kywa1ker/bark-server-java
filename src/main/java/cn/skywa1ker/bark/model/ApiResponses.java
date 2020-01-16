/*
 * Copyright (c) 2018-2022 Caratacus, (caratacus@qq.com).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.skywa1ker.bark.model;

import java.io.Serializable;

import lombok.*;

/**
 * @author hfb
 * @date 20/1/15
 * @param <T>
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponses<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表示接口调用成功
     */
    public static final int SUCCESS = 200;
    /**
     * 表示接口调用失败
     */
    public static final int FAIL = -1;
    /**
     * 表示没有权限调用该接口
     */
    public static final int NO_PERMISSION = -2;

    public static final String SUCC_MSG = "成功";
    public static final String FAIL_MSG = "失败";

    private String message = SUCC_MSG;
    private Integer code = SUCCESS;
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
