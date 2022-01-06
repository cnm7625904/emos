package com.example.emos.config.shiro;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.log.Log;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * copyright (C), 2022, 运达科技有限公司
 * fileName  JwtUtil
 *
 * @author 王玺权
 * date  2022-01-06 11:59
 * description 生成token
 * reviser
 * revisionTime
 */
@Component
@Slf4j
public class JwtUtil {
    /**
     * 密钥
     */
    @Value("${emos.jwt.secret}")
    private String secret;

    /**
     * 过期时间
     */
    @Value("${emos.jwt.expire}")
    private int expire;

    /**
     * 创建Token
     * @param userId 用户id
     * @return
     */
    public String createToken(int userId){
        //偏移5天
        DateTime date = DateUtil.offset(new Date(), DateField.DAY_OF_YEAR, 5);
        //传入密钥
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTCreator.Builder builder = JWT.create();
        String token = builder
                //根据用户id生成
                .withClaim("userId", userId)
                //过期时间
                .withExpiresAt(date)
                //调用加密算法
                .sign(algorithm);
        log.info("生成Token:"+token);
        return token;
    }

    /**
     * 根据用户用户token获取用户id
     * @param token
     * @return
     */
    public int getUserId(String token){
        DecodedJWT decode = JWT.decode(token);
        Integer userId = decode.getClaim("userId").asInt();
        return userId;
    }

    /**
     * 验证Token 是否可用
     * @param token
     */
    public void verifierToken(String token){
        //创建算法对象
        Algorithm algorithm = Algorithm.HMAC256(secret);
        //解密
        JWTVerifier build = JWT.require(algorithm).build();
        //验证Token
        build.verify(token);
    }
}
