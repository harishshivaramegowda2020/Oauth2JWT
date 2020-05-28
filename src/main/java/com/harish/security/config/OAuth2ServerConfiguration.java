package com.harish.security.config;

import org.springframework.beans.factory.harish.Autowired;
import org.springframework.context.harish.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.harish.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.harish.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.harish.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.harish.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.harish.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.harish.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.harish.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.harish.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class OAuth2ServerConfiguration {

	@Configuration
	@EnableResourceServer
	protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

		@Autowired
		private JwtAccessTokenConverter jwtAccessTokenConverter;

		@Override
		public void configure(final ResourceServerSecurityConfigurer resources) {
			resources.tokenStore(new JwtTokenStore(jwtAccessTokenConverter));
		}

		@Override
		public void configure(final HttpSecurity http) throws Exception {
			http.csrf().disable().authorizeRequests().anyRequest().authenticated();
		}
	}

	@Configuration
	@EnableAuthorizationServer
	protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
		@Autowired
		private JwtAccessTokenConverter jwtAccessTokenConverter;
		@Autowired
		private BCryptPasswordEncoder passwordEncoder;
		@Autowired
		private AuthenticationManager authenticationManager;

		@Override
		public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints.tokenStore(new JwtTokenStore(jwtAccessTokenConverter))
					.authenticationManager(authenticationManager).accessTokenConverter(jwtAccessTokenConverter);
		}

		@Override
		public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
			clients.inMemory().withClient("client").secret(passwordEncoder.encode("secret"))
					.authorizedGrantTypes("password", "refresh_token").refreshTokenValiditySeconds(300)
					.accessTokenValiditySeconds(300).scopes("read", "write");
		}
	}
}