package com.server.ssipduck.global.security.configuration;

import com.server.ssipduck.global.security.exception.CustomAccessDeniedHandler;
import com.server.ssipduck.global.security.exception.CustomAuthenticationEntryPoint;
import com.server.ssipduck.global.security.exception.filter.ExceptionHandlerFilter;
import com.server.ssipduck.global.security.exception.filter.ExceptionHandlerFilterConfiguration;
import com.server.ssipduck.global.security.filter.TokenFilter;
import com.server.ssipduck.global.security.filter.TokenFilterConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final TokenFilter tokenFilter;
    private final ExceptionHandlerFilter exceptionHandlerFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors((corsConfig) -> corsConfig.disable())
                .csrf((csrfConfig) -> csrfConfig.disable())
                .httpBasic((httpBasic) -> httpBasic.disable())

                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests((authorizeRequests) ->
                        authorizeRequests.anyRequest()
                                .permitAll()
                )

                .exceptionHandling((exceptionConfig) ->
                        exceptionConfig
                                .accessDeniedHandler(accessDeniedHandler)
                                .authenticationEntryPoint(authenticationEntryPoint)
                )
                .with(new TokenFilterConfiguration(tokenFilter), Customizer.withDefaults())
                .with(new ExceptionHandlerFilterConfiguration(exceptionHandlerFilter), Customizer.withDefaults())
                .build();
    }
}
