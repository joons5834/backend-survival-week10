package kr.megaptera.backendsurvivalweek10.application;

import io.hypersistence.tsid.TSID;
import kr.megaptera.backendsurvivalweek10.exceptions.UserAlreadyExistsException;
import kr.megaptera.backendsurvivalweek10.infrastructure.UserDetailsDao;
import kr.megaptera.backendsurvivalweek10.utils.AccessTokenGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SignupService {
    private final UserDetailsDao userDetailsDao;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenGenerator accessTokenGenerator;

    public SignupService(UserDetailsDao userDetailsDao, PasswordEncoder passwordEncoder, AccessTokenGenerator accessTokenGenerator) {
        this.userDetailsDao = userDetailsDao;
        this.passwordEncoder = passwordEncoder;
        this.accessTokenGenerator = accessTokenGenerator;
    }

    public String signup(String username, String password) {
        if (userDetailsDao.existsByUsername(username)) {
            throw new UserAlreadyExistsException();
        }

        String id = TSID.Factory.getTsid().toString();
        String encodedPassword = passwordEncoder.encode(password);
        String accessToken = accessTokenGenerator.generate(id);

        userDetailsDao.addUser(id, username, encodedPassword);
        userDetailsDao.addAccessToken(id, accessToken);

        return accessToken;
    }
}
