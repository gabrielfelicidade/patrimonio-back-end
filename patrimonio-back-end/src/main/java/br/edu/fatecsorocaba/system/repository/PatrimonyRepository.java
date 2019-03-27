package br.edu.fatecsorocaba.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.fatecsorocaba.system.model.Patrimony;

public interface PatrimonyRepository extends JpaRepository<Patrimony, Long> {

}
