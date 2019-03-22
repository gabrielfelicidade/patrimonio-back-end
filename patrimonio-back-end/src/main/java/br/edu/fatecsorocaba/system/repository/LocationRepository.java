package br.edu.fatecsorocaba.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.fatecsorocaba.system.model.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {

}
