package com.example.userBalanceApp.service.impl;

import com.example.userBalanceApp.dao.postgres.UserRepository;
import com.example.userBalanceApp.dto.AuthenticationDto;
import com.example.userBalanceApp.dto.JwtAuthenticationDto;
import com.example.userBalanceApp.exception.ExpiredTokenException;
import com.example.userBalanceApp.exception.InvalidCredentialsException;
import com.example.userBalanceApp.exception.InvalidTokenException;
import com.example.userBalanceApp.mapper.UserMapper;
import com.example.userBalanceApp.model.User;
import com.example.userBalanceApp.service.AuthService;
import com.example.userBalanceApp.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${spring.jwt.key}")
    private String jwtKey;

    /**
     * Количество часов до истечения токена
     */
    @Value("${spring.jwt.expiration}")
    private Integer jwtExpiration;

    private final UserService userService;

    private final UserRepository userRepository;

    private UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    public void authorize(HttpServletRequest request, HttpServletResponse response, String token) {
        if(isTokenExpired(token)) throw new ExpiredTokenException();

        try{
            String id = extractId(token);

            UserDetails userDetails = userMapper.toAuthUser(userService.getUserById(Long.parseLong(id)));
            SecurityContext context = SecurityContextHolder.createEmptyContext();

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            context.setAuthentication(authToken);
            SecurityContextHolder.setContext(context);
        } catch (RuntimeException ex) {
            log.error("Authorization error occured - [{}], [{}]",ex.getClass().getName(), ex.getMessage());
            throw new InvalidTokenException();
        }

    }

    @Override
    public JwtAuthenticationDto authenticate(AuthenticationDto dto) {
        Optional<User> optional = userRepository.findUserByAuthenticationData(dto.getUserData());

        if(optional.isEmpty()) throw new InvalidCredentialsException();

        User user = optional.get();
        if(!user.getPassword().equals(dto.getPassword())) throw new InvalidCredentialsException();

        return new JwtAuthenticationDto(generateToken(userMapper.toAuthUser(user)));
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration * 1000 * 3600))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    public String extractId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof User user) {
            claims.put("id", user.getId());
        }

        return generateToken(claims, userDetails);
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
        return claimsResolvers.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractId(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

}
