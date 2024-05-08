package org.ying.book.configuration;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authorization.AuthorizationManager;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
//
//@Configuration
//@EnableWebSecurity
public class SecurityConfig {

//
//    @Bean
//    SecurityFilterChain web(HttpSecurity http) throws Exception {
//        // @formatter:off
//        http.csrf(Customizer.withDefaults())
//                .authorizeHttpRequests((authorize) -> authorize
////                        .requestMatchers("/auth/**").permitAll()
////                        .requestMatchers("/second-factor", "/third-factor").access(mfaAuthorizationManager)
////                                .anyRequest().authenticated()
//                                .anyRequest().permitAll()
//                )
//                .httpBasic(Customizer.withDefaults())
//                .formLogin((form) -> form.loginPage("/login2")
////                                .addFilterBefore()
////                        .successHandler(mfaAuthenticationHandler)
////                        .failureHandler(mfaAuthenticationHandler)
//                );
////                .exceptionHandling((exceptions) -> exceptions
////                        .withObjectPostProcessor(new ObjectPostProcessor<ExceptionTranslationFilter>() {
////                            @Override
////                            public <O extends ExceptionTranslationFilter> O postProcess(O filter) {
////                                filter.setAuthenticationTrustResolver(new MfaTrustResolver());
////                                return filter;
////                            }
////                        })
////                );
//        // @formatter:on
//        return http.build();
//    }
//
////    @Bean
////    AuthorizationManager<RequestAuthorizationContext> mfaAuthorizationManager() {
////        return (authentication,
////                context) -> new AuthorizationDecision(authentication.get() instanceof MfaAuthentication);
////    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }
//    @Bean
//    AuthenticationSuccessHandler successHandler() {
//        return new SavedRequestAwareAuthenticationSuccessHandler();
//    }
//
//    @Bean
//    AuthenticationFailureHandler failureHandler() {
//        return new SimpleUrlAuthenticationFailureHandler("/login?error");
//    }
}