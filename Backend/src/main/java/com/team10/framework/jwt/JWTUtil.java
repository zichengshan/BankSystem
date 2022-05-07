package com.team10.framework.jwt;

import com.team10.entity.User;
import com.team10.framework.exception.MyException;
import org.springframework.util.StringUtils;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JWTUtil {
    public static String token="token";
    //create token
    public static String jwt_secret="wcx@achieve@1998.";
    public static long jwt_expr = 3600*24*1000;

    public static String sign(User user){
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMills = System.currentTimeMillis();
        Date date = new Date(nowMills);
        Map<String,Object> claims = new HashMap<>();
        claims.put("id",user.getId());
        claims.put("username",user.getUsername());
        String subject = user.getUsername();
        JwtBuilder builder =
                Jwts.builder().setClaims(claims).setId(UUID.randomUUID().toString()).setIssuedAt(date).setSubject(subject)
                .signWith(signatureAlgorithm,jwt_secret);
        Date exprDate = new Date(nowMills + jwt_expr);
        builder.setExpiration(exprDate);
        return builder.compact();
    }

    //verify token
    public static boolean verify(String token){
       try {
           if (StringUtils.isEmpty(token)) {
               return false;
           }
           Jwts.parser()
                   .setSigningKey(jwt_secret).parseClaimsJws(token).getBody();
           return true;
       }
       catch (Exception e){
           e.printStackTrace();
           return false;
       }
    }
    //get user info

    public static User getUser(String token){
        try {
            if(StringUtils.isEmpty(token)){
                throw new MyException("token can not be null");
            }
            if(verify(token)){
                Claims claims = Jwts.parser().setSigningKey(jwt_secret).parseClaimsJws(token).getBody();
                User user = new User();
                user.setId(Integer.parseInt(claims.get("id")+""));
                user.setUsername(claims.get("username")+"");
                return user;
            }else{
                throw new MyException("overtime or illegal token");
            }
        } catch (Exception e) {
            throw new MyException("overtime or illegal token");
        }
    }
}
