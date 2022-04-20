package ru.otus.libraryapplication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    public void configure( HttpSecurity http ) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers("/actuator/*").permitAll()
                .and()
                .authorizeRequests().antMatchers("/authors/*").hasRole("ADMIN")
                .and()
                .authorizeRequests().antMatchers("/genres/*").hasRole("ADMIN")
                .and()
                .authorizeRequests().antMatchers("/books/delete").hasRole("ADMIN")
                .and()
                .authorizeRequests().antMatchers("/comments/delete").hasRole("ADMIN")
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin();
    }

    @Override
    public void configure( AuthenticationManagerBuilder auth ) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}
