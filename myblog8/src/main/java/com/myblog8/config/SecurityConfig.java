package com.myblog8.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration //Indicates that this class is a configuration class.
@EnableWebSecurity //Enables the Spring Security web security features
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //here we are securing the URL (HttpSecurity), which url who can access !
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
//                .antMatchers(HttpMethod.POST,"/api/**").hasRole("ADMIN")
//                .antMatchers(HttpMethod.GET, "/api/**").hasRole("USER")
//                .antMatchers(HttpMethod.GET, "/api/**").hasAnyRole("USER","ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
//                .and()
//                .logout()
//                .logoutUrl("/api/auth/logout") // The URL where the logout will be triggered
//                .logoutSuccessHandler(logoutSuccessHandler()) // Custom logout success handler
//                .permitAll();
    }
    @Override
      protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());


//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails user = User.builder().username("pritam").password(passwordEncoder()
//                .encode("password")).roles("USER").build();
//
//        UserDetails admin = User.builder().username("admin").password(passwordEncoder()
//                .encode("admin")).roles("ADMIN").build();
//        return new InMemoryUserDetailsManager(user, admin);
    }
}