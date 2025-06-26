package com.liu.springai_demo.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Title: 1
 * @Author itmei
 * @Package com.liu.springai_demo.entity.vo
 * @Date 2025/6/26 20:43
 * @description:
 */
@Data
@NoArgsConstructor
public class Result {
    private Integer ok;
    private String msg;

    private Result(Integer ok, String msg) {
        this.ok = ok;
        this.msg = msg;
    }

    public static Result ok() {
        return new Result(1, "ok");
    }

    public static Result fail(String msg) {
        return new Result(0, msg);
    }
}
