package com.promotion.handwriting.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Slf4j
@Service
public class JwtService {

    @Value("${JWT.ISSUER:test}")
    private String issuer;

    @Value("${JWT.SECRET:test}")
    private String secret;

    @Value("${JWT.EXPIRE_MINUTE:10}")
    private int expireMinute;

    public String createJwt(String username) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, expireMinute);

        return JWT.create()
                .withIssuer(issuer)
                .withClaim("id", username)
                .withExpiresAt(calendar.getTime())
                .sign(Algorithm.HMAC256(secret));
    }

    public String getUserName(String jwtToken) {
        try {
            DecodedJWT decode = JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer(issuer)
                    .build()
                    .verify(jwtToken);
            return decode.getClaim("id").asString();
        } catch (TokenExpiredException e) {
            throw new IllegalArgumentException("jwt token has expired");
        }
    }
}
