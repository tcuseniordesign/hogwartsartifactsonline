package edu.tcu.cs.hogwartsartifactsonline.configuration;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import edu.tcu.cs.hogwartsartifactsonline.security.CustomBasicAuthenticationEntryPoint;
import edu.tcu.cs.hogwartsartifactsonline.security.CustomBearerTokenAccessDeniedHandler;
import edu.tcu.cs.hogwartsartifactsonline.security.CustomBearerTokenAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class SecurityConfiguration {

    @Value("${jwt.public.key}")
    RSAPublicKey key;

    @Value("${jwt.private.key}")
    RSAPrivateKey priv;

    @Autowired
    private CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;

    @Autowired
    private CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint;

    @Autowired
    private CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // We are using lambda DSL here: https://spring.io/blog/2019/11/21/spring-security-lambda-dsl
        // If you cannot understand the customizer concept, try to add a break point and use debug to step into some method calls
        http
                .cors(Customizer.withDefaults())
                // It is recommended to secure your application at the API endpoint level
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                                .antMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("ROLE_admin") // protect the endpoint
                                .antMatchers(HttpMethod.POST, "/users/**").hasAuthority("ROLE_admin") // protect the endpoint
                                .antMatchers(HttpMethod.PUT, "/users/**").hasAuthority("ROLE_admin") // protect the endpoint
//                              .antMatchers(HttpMethod.DELETE, "/users/**").hasRole("admin")
                                .antMatchers("/h2-console/**").permitAll() // protect the endpoint
                                // Disallow everything else...
                                .anyRequest().authenticated() // Always a good idea to put this as last
                )
                .headers(headers -> headers.frameOptions().disable()) // for H2 console access
                .csrf(csrf -> csrf
                        .ignoringAntMatchers("/auth/login")
                        .ignoringAntMatchers("/h2-console/**"))
                .httpBasic(httpBasic ->
                        httpBasic.authenticationEntryPoint(customBasicAuthenticationEntryPoint))
                .oauth2ResourceServer(oauth2ResourceServer -> {
                    oauth2ResourceServer.jwt().and()
                            .authenticationEntryPoint(customBearerTokenAuthenticationEntryPoint)
                            .accessDeniedHandler(customBearerTokenAccessDeniedHandler);
                }) // Configures the spring boot application as an OAuth2 Resource Server which authenticates all
                // the incoming requests (except the ones excluded above) using JWT authentication.
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        /*
        exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                )
         */
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.key).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.key).privateKey(this.priv).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        // Let’s say that that your authorization server communicates authorities in a custom claim called authorities.
        // In that case, you can configure the claim that JwtAuthenticationConverter should inspect, like so:
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");

        // You can also configure the authority prefix to be different as well.
        // For example, you can change it to ROLE_ like so:
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}
