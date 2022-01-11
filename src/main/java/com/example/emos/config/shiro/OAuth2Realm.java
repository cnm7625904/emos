package com.example.emos.config.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * copyright (C), 2022, 运达科技有限公司
 * fileName  OAuth2Realm
 *
 * @author 王玺权
 * date  2022-01-06 15:12
 * description 认证与授权
 * reviser
 * revisionTime
 */
@Component
public class OAuth2Realm extends AuthorizingRealm{
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 授权方法 验证权限时调用
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //TODO 查询用户权限列表
        //TODO 把权限列表添加到对象中
        return simpleAuthorizationInfo;
    }

    /**
     * 认证token 登录时调用
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //TODO 从令牌中获取userId，然后检测该用户是否被冻结
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo();
        //TODO 往对象中添加用户信息、Token字符串
        return simpleAuthenticationInfo;
    }

    /**
     * 判断是否符合需要对象
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }
}
