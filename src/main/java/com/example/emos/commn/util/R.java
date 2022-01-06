package com.example.emos.commn.util;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * copyright (C), 2021, 运达科技有限公司
 * fileName  R
 *
 * @author 王玺权
 * date  2021-12-30 15:05
 * description 接口返回数据
 * reviser
 * revisionTime
 */
public class R extends HashMap<String,Object>{
    public R() {
        put("code",200);
        put("msg","success");
    }

    @Override
    public R put(String key,Object value){
        super.put(key, value);
        return this;
    }

    public static R ok(){
        return new R();
    }
    public static R ok(String msg){
        R r=new R();
        r.put("msg",msg);
        return r;
    }
    public static R ok(Map<String,Object> map){
        R r=new R();
        r.putAll(map);
        return r;
    }

    public static R error(int code,String msg){
        R r=new R();
        r.put("code",code);
        r.put("msg",msg);
        return r;
    }

    public static R error(String msg){
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR,msg);
    }
}
