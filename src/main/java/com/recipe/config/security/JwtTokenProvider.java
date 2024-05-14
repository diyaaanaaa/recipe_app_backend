package com.recipe.config.security;

import com.recipe.util.exceptions.AuthorizationException;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.Key;
import java.util.*;

@Component
@Log4j2
public class JwtTokenProvider implements Serializable {

    @Value("${jwt.access.token.expired}")
    private long ACCESS_TOKEN_VALIDITY;

    @Value("${jwt.secret}")
    private String JWT_TOKEN_SECRET;

    @Value("${jwt.header}")
    private String AUTHORIZATION_HEADER;

    @PostConstruct
    protected void init() {
        JWT_TOKEN_SECRET = Base64.getEncoder().encodeToString(JWT_TOKEN_SECRET.getBytes());
    }

    public String createJwt(long userId, String name, String email, List<String> roles) {

        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", roles);
        claims.put("userId", userId);
        claims.put("name", name);

        Date now = new Date();
        Date validity = new Date(now.getTime() + ACCESS_TOKEN_VALIDITY);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) throws AuthorizationException {

        try {
            Claims claim = this.getClaims(token);
            return !claim.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthorizationException(e.getMessage());
        }
    }

    public Authentication getAuthentication(String token) {

        String username = getUsername(token);

        String[] roles = getRoles(token);

        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String r : roles) {

            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(r.trim());
            grantedAuthorities.add(grantedAuthority);
        }

        UserDetails userDetails = new User(username, "[PROTECTED]", grantedAuthorities);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String[] getRoles(String token) {
        String role = getClaims(token).get("role").toString();

        return role.substring(1, role.length() - 1).split(", ");
    }

    public Claims getClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String resolveToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        return header == null ? null : header.replaceAll("Bearer ", "");
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_TOKEN_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
