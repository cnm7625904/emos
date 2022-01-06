package com.example.emos.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * copyright (C), 2022, 运达科技有限公司
 * fileName  OAuth2Token
 *
 * @author 王玺权
 * date  2022-01-06 15:00
 * description 转换为jwt对象 然后交给  认证授权
 * reviser
 * revisionTime
 * @see OAuth2Realm
 */
public class OAuth2Token implements AuthenticationToken {
    private String token;

    public OAuth2Token(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
