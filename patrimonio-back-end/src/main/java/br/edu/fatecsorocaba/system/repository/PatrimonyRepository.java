package br.edu.fatecsorocaba.system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.fatecsorocaba.system.model.Patrimony;

public interface PatrimonyRepository extends JpaRepository<Patrimony, Long> {
	List<Patrimony> findByStatus(int status);
	List<Patrimony> findByStatusNot(int status);
}
