package br.edu.fatecsorocaba.system.config;

import java.io.IOException;
import java.util.Date;

import javax.imageio.IIOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws IIOException, ServletException {
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(403);
        try {
    	res.getWriter().write("{\n\t\"title\":" + "\"Access Denied\",\n\t\"status\": \""+ res.getStatus() +"\",\n\t"
    						+ "\"detail\": \"" + authException.getMessage() + "\",\n\t"
    						+ "\"timestamp\": \""+ new Date().getTime() +"\",\n\t"
    						+ "\"developerMessage\": \"" + authException.getMessage() + "\"\n}");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}