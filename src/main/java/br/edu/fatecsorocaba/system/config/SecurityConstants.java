package br.edu.fatecsorocaba.system.config;

import javax.crypto.SecretKey;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class SecurityConstants {
	static final SecretKey SECRET = MacProvider.generateKey(SignatureAlgorithm.HS512);
	static final String TOKEN_PREFIX = "Bearer ";
	static final String HEADER_STRING = "Authorization";
	static final String LOGIN_URL = "/login";
//	Expires in one day
	static final long EXPIRATION_TIME = 86400000L;
}
