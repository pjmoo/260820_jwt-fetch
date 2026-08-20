package org.example.jwtfetch.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
//@EnableConfigurationProperties(AuthProperties.class)
public class JwtFilter extends OncePerRequestFilter {
//    private final AuthProperties p;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        System.out.println("JwtFilter.doFilterInternal");
        try {
            // 1. extractToken (cookie, header)
            String token = extractToken(request); // request -> cookie, header...
            // 2. extractClaims
            Claims claims = extractClaims(token); // jwtProvider -> claims
            // 3. Authentication
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    claims.getSubject(),
                    null,
                    AuthorityUtils.createAuthorityList("ROLE_USER")
            );
            SecurityContext context = SecurityContextHolder.getContext();
//            context.setAuthentication(null);
            context.setAuthentication(auth);
            System.out.println("인증 완료");
        } catch (Exception e) {
            e.printStackTrace();
            SecurityContextHolder.clearContext();
        }
        // 무조건 실행이 되어야함
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        // header <- 외부로 openapi 형식으로 할 때
        // cookie <- 내부에서 호출할 때
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null; // 어차피 claims 시에 문제가 생기므로...
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("accessToken")) {
                System.out.println("cookie: %s".formatted(cookie.getValue()));
                return cookie.getValue(); // JWT
            }
        }
        return null;
    }

    private final JwtProvider jwtProvider;

    private Claims extractClaims(String token) {
        return jwtProvider.parseToken(token);
    }
}
