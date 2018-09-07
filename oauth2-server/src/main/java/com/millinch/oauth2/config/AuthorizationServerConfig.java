package com.millinch.oauth2.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;

/**
 * This guy is busy, nothing left
 *
 * @author John Zhang
 */
@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties(AuthorizationServerProperties.class)
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationServerConfig.class);

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    private final AuthorizationServerProperties properties;

    @Value("${your-oauth2.privateKey}")
    private String privateKey;

    @Value("${your-oauth2.publicKey}")
    private String publicKey;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private YourUserDetailsManager userDetailsManager;

    @Autowired
    private PasswordEncoder passwordEncoder;
//
//    @Bean
//    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }

    public AuthorizationServerConfig(AuthorizationServerProperties properties) throws Exception {
        this.properties = properties;
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(this.privateKey);
        converter.setVerifierKey(this.publicKey);
        return converter;
    }

    @Bean
    public JwtTokenStore jwtTokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JdbcTokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setClientDetailsService(clientDetailsService);
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setTokenEnhancer(accessTokenConverter());
        return defaultTokenServices;
    }


    @Bean
    protected AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security)
            throws Exception {
        if (this.properties.getCheckTokenAccess() != null) {
            security.checkTokenAccess(this.properties.getCheckTokenAccess());
        }
        if (this.properties.getTokenKeyAccess() != null) {
            security.tokenKeyAccess(this.properties.getTokenKeyAccess());
        }
        if (this.properties.getRealm() != null) {
            security.realm(this.properties.getRealm());
        }
        security.passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authorizationCodeServices(authorizationCodeServices())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsManager)
                .tokenServices(tokenServices())
                .tokenStore(tokenStore())
//                .reuseRefreshTokens(true)
                .accessTokenConverter(accessTokenConverter())
                .approvalStoreDisabled();

//        endpoints.addInterceptor(new HandlerInterceptorAdapter() {
//            @Override
//            public void postHandle(HttpServletRequest request,
//                                   HttpServletResponse response, Object handler,
//                                   ModelAndView modelAndView) throws Exception {
//                if (modelAndView != null
//                        && modelAndView.getView() instanceof RedirectView) {
//                    RedirectView redirect = (RedirectView) modelAndView.getView();
//                    String url = redirect.getUrl();
//                    if (url.contains("code=") || url.contains("error=")) {
//                        HttpSession session = request.getSession(false);
//                        if (session != null) {
//                            session.invalidate();
//                        }
//                    }
//                }
//            }
//        });
    }
}
