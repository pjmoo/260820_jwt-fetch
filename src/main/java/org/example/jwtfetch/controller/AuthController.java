package org.example.jwtfetch.controller;

import lombok.RequiredArgsConstructor;
import org.example.jwtfetch.auth.AuthCookieUtil;
import org.example.jwtfetch.auth.JwtProvider;
import org.example.jwtfetch.auth.RefreshTokenRepository;
import org.example.jwtfetch.dto.LoginForm;
import org.example.jwtfetch.service.UserAccountService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class AuthController {
    private final UserAccountService userAccountService;
    private final AuthCookieUtil authCookieUtil;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Validated @RequestBody LoginForm dto) {
//        String token = userAccountService.login(dto.username(), dto.password());
        UserAccountService.TokenResult result = userAccountService.login(dto.username(), dto.password());
        ResponseCookie accessTokenCookie = authCookieUtil.createAccessTokenCookie(result.accessToken());
        ResponseCookie refreshTokenCookie = authCookieUtil.createRefreshTokenCookie(result.refreshToken());
        return ResponseEntity.ok()
//                .header("Set-Cookie", accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE,
                        accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE,
                        refreshTokenCookie.toString())
                .body(result.accessToken());
    }

    @DeleteMapping("/logout")
    // import org.springframework.security.core.Authentication;
    public ResponseEntity<Void> logout(
//            @CookieValue("refreshToken") String refreshToken
            Authentication authentication
    ) {
        ResponseCookie accessTokenCookie = authCookieUtil.deleteAccessTokenCookie();
        // authentication.getName() -> username
//        Claims claims = jwtProvider.parseToken(refreshToken);
//        refreshTokenRepository.deleteById(claims.getId());
        refreshTokenRepository.deleteAll(
                refreshTokenRepository.findByUsername(authentication.getName())
        );
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE,
                        accessTokenCookie.toString())
                .build();
    }

    private final JwtProvider jwtProvider;

    private final RefreshTokenRepository refreshTokenRepository;
}
