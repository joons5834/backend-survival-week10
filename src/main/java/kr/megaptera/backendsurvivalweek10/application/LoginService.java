package kr.megaptera.backendsurvivalweek10.application;

import kr.megaptera.backendsurvivalweek10.infrastructure.UserDetailsDao;
import kr.megaptera.backendsurvivalweek10.utils.AccessTokenGenerator;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginService {
    private final AccessTokenGenerator accessTokenGenerator;
    private final UserDetailsDao userDetailsDao;
    private final PasswordEncoder passwordEncoder;

    public LoginService(AccessTokenGenerator accessTokenGenerator, UserDetailsDao userDetailsDao, PasswordEncoder passwordEncoder) {
        this.accessTokenGenerator = accessTokenGenerator;
        this.userDetailsDao = userDetailsDao;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(String username, String password) {
        return userDetailsDao.findByUsername(username)
                .filter(userDetails ->
                        passwordEncoder.matches(
                                password, userDetails.getPassword()))
                .map(userDetails -> {
                    String id = userDetails.getUsername();
                    String accessToken = accessTokenGenerator.generate(id);
                    userDetailsDao.addAccessToken(id, accessToken);
                    return accessToken;
                })
                .orElseThrow(() -> new BadCredentialsException("Login Failed"));

    }
}
