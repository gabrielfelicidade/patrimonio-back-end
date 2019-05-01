package br.edu.fatecsorocaba.system.config;

import static br.edu.fatecsorocaba.system.config.SecurityConstants.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import br.edu.fatecsorocaba.system.service.CustomUserDetailService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.TextCodec;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	private final CustomUserDetailService customUserDetailService;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager,
			CustomUserDetailService customUserDetailService) {
		super(authenticationManager);
		this.customUserDetailService = customUserDetailService;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, 
									HttpServletResponse response, 
									FilterChain chain) throws IOException, ServletException {
		String header = request.getHeader(HEADER_STRING);
		if(header == null || !header.startsWith(TOKEN_PREFIX)) {
			SecurityContextHolder.getContext().setAuthentication(null);
			chain.doFilter(request, response);
			return;
		}
		try {
	         UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(request);
	         SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		} catch (Exception e) {
			SecurityContextHolder.getContext().setAuthentication(null); // new
		}
		chain.doFilter(request, response);
	}
	
	private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		if (token == null) return null;
		String username = Jwts
				.parser()
				.setSigningKey(TextCodec.BASE64.encode(SECRET.getEncoded()))
				.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
				.getBody()
				.getSubject();
		UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
		return username != null ? new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities()) : null;
	}
}
