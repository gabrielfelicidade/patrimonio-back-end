package br.edu.fatecsorocaba.system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.edu.fatecsorocaba.system.model.Patrimony;

public interface PatrimonyRepository extends JpaRepository<Patrimony, Long> {
	List<Patrimony> findByStatus(int status);
	List<Patrimony> findByStatusNot(int status);
	@Query("select p from Patrimony p where p.status = 0 and year(p.writeOffDate) = ?1 and month(p.writeOffDate) = ?2")
	List<Patrimony> findByYearAndMonth(int year, int month);
}
