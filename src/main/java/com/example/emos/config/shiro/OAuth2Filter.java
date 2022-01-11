package com.example.emos.config.shiro;

import cn.hutool.core.util.StrUtil;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * copyright (C), 2022, 运达科技有限公司
 * fileName  OAuth2Filter
 *
 * @author 王玺权
 * date  2022-01-10 14:49
 * description 拦截过滤器
 * reviser
 * revisionTime
 */
@Component
/**
 * 注解为多例对象
 */
@Scope("prototype")
public class OAuth2Filter extends AuthenticatingFilter {
    @Autowired
    private ThreadLocalToken threadLocalToken;
    @Value("${emos.jwt.cache-expire}")
    private int cacheExpire;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     *  拦截请求后，把令牌字符串封装为令牌对象
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        //获取token
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        String token = getRequestToken(request);
        if(StrUtil.isBlank(token)){
            return null;
        }
        return new OAuth2Token(token);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return false;
    }

    /**
     * 获取Token
     * @param servletRequestS
     */
    private String getRequestToken(HttpServletRequest servletRequestS){
        String token = servletRequestS.getHeader("token");
        if(StrUtil.isBlank(token)){
            token=servletRequestS.getParameter("token");
        }
        return token;
    }


    /**
     * 拦截请求，判断是否需要贝被shiro请求
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest servletRequest= (HttpServletRequest) request;
        //Ajax提交application/json数据时，会先发出opition请求
        //这里放行Options请求，不需要Shiro处理
        if(((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name())){
            return true;
        }
        //除了options外全部都要被shrio处理
        return false;
    }

}
