package org.ying.book.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.ying.book.enums.SystemSettingsEnum;
import org.ying.book.pojo.SystemSetting;
import org.ying.book.service.SystemSettingsService;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class JwtUtil {
    public static final String JWT_KEY = "leon";
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @Resource
    ObjectMapper objectMapper;

    @Resource
    private SystemSettingsService SystemSettingsService;
    @Autowired
    private SystemSettingsService systemSettingsService;

    public SecretKeySpec generalKey(){

        byte[] encodedKey = Base64.getEncoder().encode(secret.getBytes());
        System.out.println(encodedKey);
        SecretKeySpec key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "HmacSHA256");
        return key;
    }

    public String createJWT(String id, Object subject, long ttlMillis) throws JsonProcessingException {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        long nowMillis = System.currentTimeMillis();//生成JWT的时间
        Date now = new Date(nowMillis);
        SecretKey key = generalKey();//生成签名的时候使用的秘钥secret,这个方法本地封装了的，一般可以从本地配置文件中读取，切记这个秘钥不能外露哦。它就是你服务端的私钥，在任何场景都不应该流露出去。一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
        JwtBuilder builder = Jwts.builder() //这里其实就是new一个JwtBuilder，设置jwt的body
//                .setClaims(claims)            //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setId(id)                    //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setIssuedAt(now)            //iat: jwt的签发时间
                .setSubject(objectMapper.writeValueAsString(subject))        //sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .signWith(signatureAlgorithm, key);//设置签名使用的签名算法和签名使用的秘钥
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);        //设置过期时间
        }
        return builder.compact();            //就开始压缩为xxxxxxxxxxxxxx.xxxxxxxxxxxxxxx.xxxxxxxxxxxxx这样的jwt
    }

    public String createJWT(String id, Object subject) throws JsonProcessingException {
        Integer expiration = Integer.parseInt(systemSettingsService.getSystemSettingValueByName(SystemSettingsEnum.TOKEN_EXPIRATION).toString());
        return this.createJWT(id, subject, expiration * 60 * 60 * 1000);
    }

    public Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        try {
            // 解析JWT并提取其claims
            Claims claims = parseJWT(token);

            // 获取JWT的过期时间（exp）并将其与当前时间进行比较
            long expirationTime = claims.getExpiration().getTime();
            long currentTime = System.currentTimeMillis();

            // 如果过期时间早于当前时间，则表示JWT已过期
            return expirationTime < currentTime;
        } catch (ExpiredJwtException e) {
            // 如果捕获到ExpiredJwtException异常，说明JWT已过期
            return true;
        } catch (Exception e) {
            return true;
        }
    }
    /**
     * 由字符串生成加密的key
     * @return
     */
//    public static SecretKeySpec generalKey(){
//
//        String stringKey = JWT_KEY;
//        byte[] bytes = Base64.decodeBase64(stringKey);
//        SecretKeySpec key = new SecretKeySpec(bytes, 0, bytes.length, "AES");
//        return key;
//    }
//
//    public String generateToken(String username) {
//        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
//        long nowMillis = System.currentTimeMillis();//生成JWT的时间
//        Date now = new Date(nowMillis);
//        SecretKey key = generalKey();
//        JwtBuilder builder = Jwts.builder() //这里其实就是new一个JwtBuilder，设置jwt的body
////                .setClaims(claims)            //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
//                .setId("1")                    //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
//                .setIssuedAt(now)            //iat: jwt的签发时间
//                .setSubject(username)        //sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
//                .signWith(signatureAlgorithm, key);//设置签名使用的签名算法和签名使用的秘钥
//        if (expiration >= 0) {
//            long expMillis = nowMillis + expiration;
//            Date exp = new Date(expMillis);
//            builder.setExpiration(exp);        //设置过期时间
//        }
//        return builder.compact();
////        System.out.println(secret+username);
////        Date now = new Date();
////        Date expiryDate = new Date(now.getTime() + expiration);
////
////        return Jwts.builder()
////                .setSubject(username)
////                .setIssuedAt(now)
////                .setExpiration(expiryDate)
////                .signWith(SignatureAlgorithm.HS512, secret)
////                .compact();
//    }

//    public String getUsernameFromToken(String token) {
//        Claims claims = Jwts.parser()
//                .setSigningKey(secret)
//                .parseClaimsJws(token)
//                .getBody();
//
//        return claims.getSubject();
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
}

