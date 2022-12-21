package edu.tcu.cs.hogwartsartifactsonline.controller;

import edu.tcu.cs.hogwartsartifactsonline.domain.Result;
import edu.tcu.cs.hogwartsartifactsonline.domain.StatusCode;
import edu.tcu.cs.hogwartsartifactsonline.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Generate a JWT token if username and password has been authenticated by the BasicAuthenticationFilter.
     * In summary, this filter is responsible for processing any request that has an HTTP request header of Authorization
     * with an authentication scheme of Basic and a Base64-encoded username:password token.
     *
     * BasicAuthenticationFilter will prepare the Authentication object for this login method.
     * Note: before this login method gets called, Spring Security already authenticated the username and password through Basic Auth
     * Only successful authentication can make it to this method.
     * @return JWT token
     */
    @PostMapping("/login")
    public Result login(Authentication authentication) throws Exception {
        System.out.println(authentication.getAuthorities());
        return new Result(true, StatusCode.SUCCESS, "JWT Token and User Info", authService.createLoginInfo(authentication));
    }
}
