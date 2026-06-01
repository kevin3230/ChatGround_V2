package com.chatground.utility;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.chatground.security.UserPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	public final String secretKeyStr;
	public final int validSeconds;
	private final SecretKey secretKey;
	private final JwtParser jwtParser;
	
	public JwtUtil(String secretKeyStr, int validSeconds) {
		this.secretKeyStr = secretKeyStr;
		this.validSeconds = validSeconds;
		this.secretKey = Keys.hmacShaKeyFor(secretKeyStr.getBytes(StandardCharsets.UTF_8));
		this.jwtParser = Jwts.parser().verifyWith(secretKey).build();
	}
	
	public String createToken(UserPrincipal user) {
        // 計算過期時間
        long expirationMillisecond = Instant.now()
                .plusSeconds(validSeconds)
                .getEpochSecond()
                * 1000;

        // 準備 payload 內容
        Claims claims = Jwts.claims()
                .issuedAt(new Date())
                .subject(String.valueOf(user.getId()))
                .expiration(new Date(expirationMillisecond))
                .add("sender", String.valueOf(user.getId()))
                .add("senderNickname", user.getNickName())
                .build();

        // 簽名後產生 JWT
        return Jwts.builder()
                .claims(claims)
                .signWith(secretKey)
                .compact();
    }
	
	//解析jwt的payload
	public Claims parseToken(String jwt) throws JwtException, UnsupportedJwtException {
        return jwtParser.parseSignedClaims(jwt).getPayload();
    }
}
