package br.edu.fatecsorocaba.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.fatecsorocaba.system.model.Log;

public interface LogRepository extends JpaRepository<Log, Integer> {

}
