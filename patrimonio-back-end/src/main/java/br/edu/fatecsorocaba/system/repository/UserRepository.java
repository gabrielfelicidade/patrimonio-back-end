package br.edu.fatecsorocaba.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.fatecsorocaba.system.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
