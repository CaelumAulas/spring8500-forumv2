package br.com.alura.forum.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.forum.security.service.UserService;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	private TokenManager tokenManager;
	@Autowired
	private UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = getTokenFromRequest(request);

		if (tokenManager.isValid(token)) {
			// pega usuário
			Long userId = tokenManager.getUserId(token);
			UserDetails user = userService.loadUserById(userId);

			// deixa usuário disponivel no contexto de segurança
			SecurityContext context = SecurityContextHolder.getContext();
			Authentication authentication =
			new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			context.setAuthentication(authentication);
		} else {
			System.out.println(token);
		}
		
		filterChain.doFilter(request, response); //ir pro próximo filtro
		
		// lógica final requisição
	}

	private String getTokenFromRequest(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		
		if (StringUtils.hasText(authorization) && 
				authorization.startsWith("Bearer ")) {
			return authorization.substring(7);
		}
		return null;
	}

}
