package com.trinity.authentication.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY = "b24bfe05b10f876172094ffa542dd10b43437cd9934d95c844a5006e51a8038a";
    @Value("${jwt.expiration}")
    private String jwtExpiration;

    public String extractUsername(String jwt) {
        return this.extractClaim(jwt, Claims::getSubject);
    }

    public <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(jwt);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return this.generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
        Map<String, Object> extraClaims,
        UserDetails userDetails
    ) {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(this.getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        final String username = this.extractUsername(jwt);
        return username.equals(userDetails.getUsername()) && !this.isTokenExpired(jwt);
    }
        
    public boolean isTokenExpired(String jwt) {
        return this.extractExpiration(jwt).before(new Date());
    }

    private Date extractExpiration(String jwt) {
        return this.extractClaim(jwt, Claims::getExpiration);
    }
        
    private Claims extractAllClaims(String jwt) {
        return Jwts
            .parserBuilder() 
            .setSigningKey(this.getSignInKey())
            .build()
            .parseClaimsJws(jwt)
            .getBody();
    }

    private Key getSignInKey() {
       byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); 
       return Keys.hmacShaKeyFor(keyBytes);
    }

}
