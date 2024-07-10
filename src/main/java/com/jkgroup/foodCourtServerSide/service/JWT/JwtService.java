package com.jkgroup.foodCourtServerSide.service.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    //    @Value("${application.security.jwt.secret-key}")
//    private String secretKey;
    private static final String SECRET_KEY = "TwX8EwAnzM9REJseUnkWqUjgRIxb/UQh08O2f/BPAq3pWrnwuij9dqrWk3arhHGf";

//    @Value("${application.security.jwt.expiration}")
//    private long jwtExpiration;
//    @Value("${application.security.jwt.refresh-token.expiration}")
//    private long refreshExpiration;

    // 1000 represents 1000 milliseconds, which is equivalent to 1 second.
    // 60 represents 60 seconds, which is equivalent to 1 minute.
    // 60 represents 60 minutes, which is equivalent to 1 hour.
    // 24 represents 24 hours, which is equivalent to 1 day.
    // 7 represents 7 days.
    // So, 1000 * 60 * 60 * 24 * 7 calculates the number of milliseconds in 7 days.
    private long refreshExpiration = 1000 * 60 * 60 * 24 * 7;


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ADD
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities().iterator().next().getAuthority());
//        claims.put("firstNAME", userDetails.getPassword());

        return generateToken(claims, userDetails);
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
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5)) // 5 minutes. This means the token will expire 5 minutes after it is generated.
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(
            UserDetails userDetails
    ) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities().iterator().next().getAuthority());
        return generateRefreshToken(claims, userDetails, refreshExpiration);
    }

    private String generateRefreshToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
