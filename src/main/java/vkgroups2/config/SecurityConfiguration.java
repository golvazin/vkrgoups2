package vkgroups2.config;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import vkgroups2.domain.User;
import vkgroups2.repository.UserRepository;
import vkgroups2.service.VKApiHelper;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    @Autowired
    OAuth2ClientContext oauth2ClientContext;
    
    @Autowired
    private VKApiHelper vkApiHelper;
    
    @Autowired
    private UserRepository userRepository;
 

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http
        .csrf().disable()
        .antMatcher("/**")
        .authorizeRequests()
        .antMatchers("/", "/login**", "/webjars/**", "/error").permitAll()
        .anyRequest().authenticated()
        .and().logout().logoutSuccessUrl("/").permitAll()
        .and().addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
  
    }
    
    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(
        OAuth2ClientContextFilter filter) {
      FilterRegistrationBean registration = new FilterRegistrationBean();
      registration.setFilter(filter);
      registration.setOrder(-100);
      return registration;
    }
    
    private Filter ssoFilter() {
        OAuth2ClientAuthenticationProcessingFilter vkFilter = new OAuth2ClientAuthenticationProcessingFilter("/login"); 
        vkFilter.setRestTemplate(vkontakteTemplate());
        vkFilter.setTokenServices(new UserInfoTokenServices(vkontakteResource().getUserInfoUri(), vkontakte().getClientId()));
        vkFilter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
            
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                    Authentication authentication) throws IOException, ServletException {
                OAuth2AccessToken token = oauth2ClientContext.getAccessToken();
                String principalName = authentication.getName();
                if (oauth2ClientContext.getAccessToken() != null && "unknown".equals(principalName)) {
                    String id = oauth2ClientContext.getAccessToken().getAdditionalInformation().get("user_id").toString();
                    User user = vkApiHelper.retrieveVkUserInfo(id, token.getValue());
                    if (!userRepository.exists(user.getVkId())) {
                        user = userRepository.save(user);
                    }
                    authenticateUser(user);
                    
                }
                response.sendRedirect("/dashboard");
            }
        });
        return vkFilter;
    }
    private void authenticateUser(User user) {
        List<GrantedAuthority> authorities = Stream.of(new SimpleGrantedAuthority("USER"))
                .collect(Collectors.toList());
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getVkId(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
    
    @Bean
    @ConfigurationProperties("vkontakte.client")
    OAuth2ProtectedResourceDetails vkontakte() {
        return new AuthorizationCodeResourceDetails();
    }
    
    @Bean
    @ConfigurationProperties("vkontakte.resource")
    ResourceServerProperties vkontakteResource() {
        return new ResourceServerProperties();
    }
    
    
    private OAuth2RestTemplate vkontakteTemplate() {
        OAuth2RestTemplate vkTemplate = new OAuth2RestTemplate(vkontakte(), oauth2ClientContext);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON,
                MediaType.valueOf("text/javascript")));
        vkTemplate.setMessageConverters(Arrays.<HttpMessageConverter<?>> asList(converter));
        return vkTemplate;
    }
    
}
