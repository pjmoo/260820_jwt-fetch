package org.example.jwtfetch.service;

import lombok.RequiredArgsConstructor;
import org.example.jwtfetch.auth.JwtProvider;
import org.example.jwtfetch.domain.entity.UserAccount;
import org.example.jwtfetch.domain.repository.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAccountService {
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(UserAccount entity) {
        String encoded = passwordEncoder.encode(entity.getPassword());
        entity.setPassword(
                encoded
        );
        userAccountRepository.save(entity); // DB에는 암호화된 형태로 저장
    }

    private final JwtProvider jwtProvider;

    // 유저가 폼 등을 통해 입력하는 패스워드는 인코딩된 상태 X
    public String login(String username, String rawPassword) {
        // 유저가 존재하고, 그 유저의 패스워드가 입력한 패스워드와 일치하는가?
        UserAccount entity = userAccountRepository
                // optional -> get / throw
                .findByUsername(username).orElseThrow();
        String encoded = entity.getPassword();
        if (!passwordEncoder.matches(rawPassword, encoded)) {
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
        }
        // 토큰을 발부
        return jwtProvider.issueToken(username);
    }
}
