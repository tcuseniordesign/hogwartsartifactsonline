package edu.tcu.cs.hogwartsartifactsonline.service;

import edu.tcu.cs.hogwartsartifactsonline.domain.MyUserPrincipal;
import edu.tcu.cs.hogwartsartifactsonline.domain.HogwartsUser;
import edu.tcu.cs.hogwartsartifactsonline.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public void setJwtTokenProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        String token = jwtTokenProvider.createToken(authentication);
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        HogwartsUser hogwartsUser = principal.getUser();
        hogwartsUser.setPassword(""); // IMPORTANT, don't send password to the frontend!!!

        Map<String, Object> loginResultMap = new HashMap<>();
        loginResultMap.put("token", token);
        loginResultMap.put("userInfo", hogwartsUser);

        return loginResultMap;

    }
}
