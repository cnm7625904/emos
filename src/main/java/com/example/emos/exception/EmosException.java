package com.example.emos.exception;

import lombok.Data;

/**
 * copyright (C), 2021, 运达科技有限公司
 * fileName  EmosException
 *
 * @author 王玺权
 * date  2021-12-30 13:14
 * description 异常类
 * reviser
 * revisionTime
 */
@Data
public class EmosException extends RuntimeException{
    private String msg;
    private int code=500;

    public EmosException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public EmosException(String msg,Throwable throwable) {
        super(msg,throwable);
        this.msg = msg;
    }

    public EmosException(String msg,int code) {
        super(msg);
        this.msg = msg;
        this.code=code;
    }
    public EmosException(String msg,int code,Throwable throwable) {
        super(msg,throwable);
        this.msg = msg;
        this.code=code;
    }
}
