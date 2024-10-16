package com.example.server.auth.config;

import com.example.server.auth.handler.MemberAccessDeniedHandler;
import com.example.server.auth.handler.MemberAuthenticationEntryPoint;
import com.example.server.auth.handler.MemberAuthenticationFailureHandler;
import com.example.server.auth.handler.MemberAuthenticationSuccessHandler;
import com.example.server.auth.jwt.JwtAuthenticationFilter;
import com.example.server.auth.jwt.JwtTokenizer;
import com.example.server.auth.jwt.JwtVerificationFilter;
import com.example.server.auth.oauth.handler.OAuth2LoginFailureHandler;
import com.example.server.auth.oauth.handler.OAuth2LoginSuccessHandler;
import com.example.server.auth.oauth.service.CustomOAuth2UserService;
import com.example.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class SecurityConfiguration {

    private final JwtTokenizer jwtTokenizer;
    private final MemberRepository memberRepository;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService oAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {

        http
                .csrf((csrfConfig) ->
                        csrfConfig.disable()
                )
                .headers((headerConfig) ->
                        headerConfig.frameOptions(frameOptionsConfig ->
                                frameOptionsConfig.disable()
                        )
                )
                .cors(Customizer.withDefaults())
                //.cors().configurationSource(corsConfigurationSource())
                //.and()
                .sessionManagement((sessionManagementConfig) ->
                        sessionManagementConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .with(new CustomFilterConfigurer(), CustomFilterConfigurer::build)

                .exceptionHandling((exceptionConfig) ->
                        exceptionConfig.authenticationEntryPoint(new MemberAuthenticationEntryPoint()).accessDeniedHandler(new MemberAccessDeniedHandler())
                )
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .anyRequest().authenticated()
                )
//가독성을 위한 구성
//                .oauth2Login()
//                .userInfoEndpoint()
//                .userService(oAuth2UserService)
//                .and()
//                .successHandler(oAuth2LoginSuccessHandler)
//                .failureHandler(oAuth2LoginFailureHandler);

                .oauth2Login(oauth2 -> oauth2
                    .successHandler(oAuth2LoginSuccessHandler)
                    .failureHandler(oAuth2LoginFailureHandler)
                    .userInfoEndpoint(userInfoEndpointConfig ->
                            userInfoEndpointConfig.userService(oAuth2UserService)
                    )
                );

        return http.build();
    }



    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowCredentials(true);
        cors.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://thegong.site", "https://thegong.site", "https://seb43-main-025.vercel.app", "http://www.apithegong.com", "https://www.apithegong.com"));
        cors.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE", "OPTIONS"));
        cors.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }



    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {

            AuthenticationManager manager = builder.getSharedObject(AuthenticationManager.class);
            JwtAuthenticationFilter authentication = new JwtAuthenticationFilter(manager, jwtTokenizer,memberRepository); //jwtAuthenticationFilter attemptAuthentication() 메서드에서 로그인 처리
            authentication.setFilterProcessesUrl("/members/login");
            authentication.setAuthenticationSuccessHandler(new MemberAuthenticationSuccessHandler());
            authentication.setAuthenticationFailureHandler(new MemberAuthenticationFailureHandler());

            JwtVerificationFilter verification = new JwtVerificationFilter(jwtTokenizer);
            builder .addFilter(authentication)
                    .addFilterAfter(verification, JwtAuthenticationFilter.class);
        }

        public HttpSecurity build(){
            return getBuilder();
        }
    }
}

