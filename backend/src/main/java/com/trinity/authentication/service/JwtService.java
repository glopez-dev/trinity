package com.trinity.authentication.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import com.trinity.user.model.AbstractUser;
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

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    public String extractUsername(String jwt) {
        return this.extractClaim(jwt, Claims::getSubject);
    }

    public <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(jwt);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();

        // JWT crypté pour éviter d'avoir role : 'ADMIN' en clair
        if (userDetails instanceof AbstractUser) {
            AbstractUser user = (AbstractUser) userDetails;

            // Génère un UUID basé sur le type d'utilisateur et son ID
            UUID roleUuid = UUID.nameUUIDFromBytes(
                    (user.getType().name() + ":" + user.getId().toString()).getBytes()
            );

            extraClaims.put("roleId", roleUuid.toString());
        }

        return this.generateToken(extraClaims, userDetails);
    }

    public String generateToken(
        Map<String, Object> extraClaims,
        UserDetails userDetails
    ) {

        long validity = this.jwtExpiration == null 
            ? 1000 * 60 * 60 
            : this.jwtExpiration;

        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + validity))
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
       byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
       return Keys.hmacShaKeyFor(keyBytes);
    }

}
