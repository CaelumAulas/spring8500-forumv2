package br.com.alura.forum.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser("ana")
				.password(new BCryptPasswordEncoder().encode("123"))
				.roles("ADMIN")
			.and()
				.withUser("pedro")
				.password(new BCryptPasswordEncoder().encode("1234"))
				.authorities("ROLE_ALUNO")
			.and().passwordEncoder(new BCryptPasswordEncoder());
	}
}
