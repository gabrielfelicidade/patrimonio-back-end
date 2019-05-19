package br.edu.fatecsorocaba.system.config;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@SuppressWarnings("serial")
public class CustomUserDetails extends User{
	private String name;
	private int userlevel;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getUserlevel() {
		return userlevel;
	}

	public void setUserlevel(int userlevel) {
		this.userlevel = userlevel;
	}

	public CustomUserDetails(String name, String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.name = name;
		this.userlevel = authoritiesToUserLevel(authorities.toString());
	}
	
	private int authoritiesToUserLevel(String authorities) {
	    if (authorities.contains("ADMIN"))
	    	return 2;
	    else if(authorities.contains("USER"))
	    	return 1;
	    return 0;
	}
}
