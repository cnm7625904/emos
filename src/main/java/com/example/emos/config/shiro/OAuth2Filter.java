package com.example.emos.config.shiro;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.apache.http.HttpStatus;
import org.apache.http.util.TextUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

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

    /**
     * 验证是否过期
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response= (HttpServletResponse) servletResponse;
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        //设置跨域-允许
        response.setHeader("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Origin",request.getHeader("origin"));
        threadLocalToken.clear();
        String requestToken = getRequestToken(request);
        if(TextUtils.isBlank(requestToken)){
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            response.getWriter().print("无效的token");
            return false;
        }
        try {
            jwtUtil.verifierToken(requestToken);
            //Token在redis中是否过期
        } catch (TokenExpiredException e) {
            /**
             * 能查到标识客户端token过期，服务器端没过期
             */
            if(redisTemplate.hasKey(requestToken)){
                //删掉令牌
                redisTemplate.delete(requestToken);
                //生成新令牌
                int userId = jwtUtil.getUserId(requestToken);
                //覆盖原来token
                requestToken=jwtUtil.createToken(userId);
                //存放token
                redisTemplate.opsForValue().set(requestToken,userId+"",cacheExpire, TimeUnit.DAYS);
                threadLocalToken.setToken(requestToken);
            }else{
                response.setStatus(HttpStatus.SC_UNAUTHORIZED);
                response.getWriter().print("token令牌已过期");
                return false;
            }
            e.printStackTrace();
            /**
             * token错误
             */
        } catch (JWTDecodeException e){
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            response.getWriter().print("token令牌无效");
            e.printStackTrace();
            return false;
        }
        finally {

        }
        //判断认证授权是否成功
        boolean b = executeLogin(request, response);
        return b;
    }

    /**
     * 用户没有登录或者登录失败--认证失败触发
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletRequest req= (HttpServletRequest) request;
        HttpServletResponse res= (HttpServletResponse) response;
        res.setContentType("text/html");
        res.setCharacterEncoding("UTF-8");
        //设置跨域-允许
        res.setHeader("Access-Control-Allow-Credentials","true");
        res.setHeader("Access-Control-Origin",req.getHeader("origin"));
        //设置状态码
        res.setStatus(HttpStatus.SC_UNAUTHORIZED);
        try {
            res.getWriter().print(e.getMessage());
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {

        }
        return false;
    }

    /**
     * 认证失败
     * @param request
     * @param response
     * @param chain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        super.doFilterInternal(request, response, chain);
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
