package br.edu.fatecsorocaba.system.config;

public class SecurityConstants {
	static final String SECRET = "PatrimonySystem Secret";
	static final String TOKEN_PREFIX = "Bearer ";
	static final String HEADER_STRING = "Authorization";
	static final String LOGIN_URL = "/login";
//	Expires in one day
	static final long EXPIRATION_TIME = 86400000L;
}
