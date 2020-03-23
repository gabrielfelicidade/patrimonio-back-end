package br.edu.fatecsorocaba.system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.edu.fatecsorocaba.system.config.CustomUserDetails;
import br.edu.fatecsorocaba.system.model.User;
import br.edu.fatecsorocaba.system.repository.UserRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository repository;
		
	@Autowired
	public CustomUserDetailsService(UserRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = Optional.ofNullable(repository.findByUsername(username)).orElseThrow(()-> new UsernameNotFoundException("User not found"));
		List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList("ROLE_BASIC");
		if (user.getUserlevel() == 2) {
			authorityList = AuthorityUtils.createAuthorityList("ROLE_BASIC", "ROLE_INTERMEDIARY", "ROLE_ADMIN");
		}
		else if(user.getUserlevel() == 1) {
			authorityList = AuthorityUtils.createAuthorityList("ROLE_BASIC", "ROLE_INTERMEDIARY");
		}
		return new CustomUserDetails(user.getName(), user.getUsername(), user.getPassword(), authorityList);
	}
}
