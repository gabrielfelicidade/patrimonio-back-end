package br.edu.fatecsorocaba.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.fatecsorocaba.system.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
	Location findByDescription(String description);
	Location findByLocationIdNotAndDescription(Long locationId, String description);
}
