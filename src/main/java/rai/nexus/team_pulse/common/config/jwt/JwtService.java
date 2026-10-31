package rai.nexus.team_pulse.common.config.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import rai.nexus.team_pulse.common.service.user.CustomUserDetails;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
public class JwtService {

    private final JwtProperties props;
    private final SecretKey secretKey;
    private final JwtParser parser;
    private final String CLAIM_TYPE = "type";
    private final String TYPE_ACCESS = "access";
    private final String TYPE_REFRESH = "refresh";

    public JwtService(JwtProperties props) {
        this.props = props;

        byte[] keyBytes = Decoders.BASE64.decode(props.getSecret());

        if(keyBytes.length < 32) {
            throw new IllegalStateException("Secret key length must be at least 32 bytes");
        }

        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.parser = Jwts.parser().verifyWith(secretKey).build();
    }

    public String generateAccessToken(CustomUserDetails user) {

        Map<String,Object> extraClaims = Map.of("role", "role");

        return buildToken(user, TYPE_ACCESS, props.getAccessTokenExpiration(), extraClaims);
    }

    public String generateRefreshToken(CustomUserDetails user) {
        return buildToken(user, TYPE_REFRESH, props.getRefreshTokenExpiration(), Map.of());
    }

    public Instant extractExpiration(String token) {
        return parseClaims(token).getExpiration().toInstant();
    }

    public boolean isAccessToken(String token) {
        return TYPE_ACCESS.equals(parseClaims(token).get(CLAIM_TYPE, String.class));
    }

    public boolean isRefreshToken(String token) {
        return TYPE_REFRESH.equals(parseClaims(token).get(CLAIM_TYPE, String.class));
    }

    public String extractTokenId(String token) {
        return parseClaims(token).getId();
    }

    public String extractSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails user) {
        Claims claims = parseClaims(token);
        return user.getUsername().equals(claims.getSubject());
    }

    private  String buildToken(
            CustomUserDetails user,
            String type,
            Duration ttl,
            Map<String, Object> extraClaims)
    {
        Instant now = Instant.now();

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getUsername())
                .claim(CLAIM_TYPE, type)
                .claims(extraClaims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(ttl)))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public Claims parseClaims(String token) {
        return parser
                .parseSignedClaims(token)
                .getPayload();
    }

}
