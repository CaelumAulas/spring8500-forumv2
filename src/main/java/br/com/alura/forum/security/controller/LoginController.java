package br.com.alura.forum.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.security.TokenManager;
import br.com.alura.forum.security.controller.dto.input.LoginInputDto;
import br.com.alura.forum.security.controller.dto.output.AuthenticationTokenOutputDto;

@RestController
public class LoginController {
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private TokenManager tokenManager;

	@PostMapping(value="api/auth", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AuthenticationTokenOutputDto> loga(@RequestBody LoginInputDto loginInputDto) {
		try {
		Authentication authenticated = authManager.authenticate(loginInputDto.buildAuthentication());
		
		String token = tokenManager.generate(authenticated);
		
		return ResponseEntity.ok(new AuthenticationTokenOutputDto("Bearer", token));
		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}
	}
}
