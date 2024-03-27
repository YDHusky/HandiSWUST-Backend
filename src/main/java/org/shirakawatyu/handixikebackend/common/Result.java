package org.shirakawatyu.handixikebackend.common;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@Accessors(chain = true, fluent = true)
public class Result implements Serializable {
    @Serial
    private static final long serialVersionUID = 1891139821031007141L;
    private boolean success;
    private int code;
    private String msg;
    private Object data;

    private Result() {
    }

    public static Result ok() {
        Result result = new Result();
        result.success(true);
        return result;
    }

    public static Result fail() {
        Result result = new Result();
        result.success(false);
        return result;
    }


}
