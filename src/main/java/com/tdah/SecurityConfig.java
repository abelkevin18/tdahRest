package com.tdah;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tdah.service.impl.UserSecurityServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserSecurityServiceImpl userDetailsService;
	
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.headers().frameOptions().sameOrigin().and()
		.authorizeRequests()
		.antMatchers("/assets/**","/css/**","/datatable/*","/img/**","/js/**","/api/**","/usuario/**").permitAll()
//.antMatchers("/**").access("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_DIRECTOR')")
		
		.anyRequest()
		.authenticated()
		.and()
		.formLogin().loginPage("/autenticacion/login").defaultSuccessUrl("/").permitAll()
		.and()
		.logout().permitAll()
		.and().exceptionHandling().accessDeniedPage("/403")
		.and().csrf().disable();
		//.and()
		//.httpBasic();
	}
	
}
