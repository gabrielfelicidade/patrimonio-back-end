package br.edu.fatecsorocaba.system.config;

import static br.edu.fatecsorocaba.system.config.SecurityConstants.*;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import br.edu.fatecsorocaba.system.model.User;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		try {
	    	User user = null;
		    try {
		    	user = new ObjectMapper().readValue(request.getInputStream(), User.class);
		    }
		    catch (UnrecognizedPropertyException exception){
		    	user = new User();
		    	user.setUsername("");
		    	user.setPassword("");
		    }
			return this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		} catch (IOException e) {
			throw new RuntimeException(e); 
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, 
											HttpServletResponse response, 
											FilterChain chain, 
											Authentication authResult) throws IOException, ServletException  {
		CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
		Header<?> header = Jwts.header();
		header.setType("JWT");
		//
		String token = Jwts
				.builder()
				.setHeader((Map<String, Object>)header)
				.setSubject(customUserDetails.getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, TextCodec.BASE64.encode(SECRET.getEncoded()))
				.claim("userlevel", customUserDetails.getUserlevel())
				.claim("name", customUserDetails.getName())
				.compact();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
		response.getWriter().write(String.format("{\"token\": \"%s\"}", token));
	}
}
