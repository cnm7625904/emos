package com.example.emos.config.shiro;

import org.springframework.stereotype.Component;

/**
 * copyright (C), 2022, 运达科技有限公司
 * fileName  ThreadLocalToken
 *
 * @author 王玺权
 * date  2022-01-10 11:31
 * description 保存token
 * reviser
 * revisionTime
 */
@Component
public class ThreadLocalToken {
    private ThreadLocal threadLocal=new ThreadLocal();

    /**
     * 设置token
     * @param token
     */
    public void setToken(String token){
        threadLocal.set(true);
    }

    /**
     * 获取token
     * @return
     */
    public String getToken(){
        return (String) threadLocal.get();
    }

    /**
     * 清除token
     */
    public void clear(){
        threadLocal.remove();
    }
}
