package edu.tcu.cs.hogwartsartifactsonline.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class handles unsuccessful basic authentication.
 * We implement AuthenticationEntryPoint and then delegate the exception handler to HandlerExceptionResolver.
 */
@Component
public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Here we've injected the DefaultHandlerExceptionResolver and delegated the handler to this resolver.
     * This security exception can now be handled with controller advice with an exception handler method.
     */
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.addHeader("WWW-Authenticate", "Basic realm=\"Realm\"");
        resolver.resolveException(request, response, null, authException);
    }
}
