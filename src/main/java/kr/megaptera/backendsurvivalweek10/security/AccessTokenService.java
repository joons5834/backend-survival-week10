package kr.megaptera.backendsurvivalweek10.security;

import kr.megaptera.backendsurvivalweek10.infrastructure.UserDetailsDao;
import kr.megaptera.backendsurvivalweek10.utils.AccessTokenGenerator;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenService {

    private final UserDetailsDao userDetailsDao;
    private final AccessTokenGenerator accessTokenGenerator;

    public AccessTokenService(UserDetailsDao userDetailsDao, AccessTokenGenerator accessTokenGenerator) {
        this.userDetailsDao = userDetailsDao;
        this.accessTokenGenerator = accessTokenGenerator;
    }

    public Authentication authenticate(String accessToken) {
        if (!accessTokenGenerator.verify(accessToken)) {
            return null;
        }
        return userDetailsDao.findByAccessToken(accessToken)
                .map(userDetails ->
                        UsernamePasswordAuthenticationToken.authenticated(
                                userDetails.getUsername(),
                                userDetails.getPassword(),
                                userDetails.getAuthorities()))
                .orElse(null);
    }
}