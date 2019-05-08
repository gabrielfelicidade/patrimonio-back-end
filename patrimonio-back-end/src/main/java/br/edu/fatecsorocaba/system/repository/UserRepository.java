package br.edu.fatecsorocaba.system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.fatecsorocaba.system.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
	List<User> findByUserIdNot(Long userId);
}
