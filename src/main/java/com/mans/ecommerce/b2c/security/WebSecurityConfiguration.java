package com.mans.ecommerce.b2c.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter
{

    private CustomerDetailsService customerDetailsService;

    public WebSecurityConfiguration(CustomerDetailsService customerDetailsService)
    {
        this.customerDetailsService = customerDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http)
            throws Exception
    {
        http.authorizeRequests()
            .antMatchers("/products/**").permitAll()
            .antMatchers("/auth/**").permitAll()
            .antMatchers("/carts/anonymous").permitAll()
            .anyRequest()
            .authenticated();

        // Disable CSRF (cross site request forgery)
        http.csrf().disable();

        // No session will be created or used by spring security
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(new JwtTokenFilter(customerDetailsService), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override protected AuthenticationManager authenticationManager()
            throws Exception
    {
        return super.authenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder(12);
    }
}
