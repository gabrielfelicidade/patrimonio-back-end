package br.edu.fatecsorocaba.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.fatecsorocaba.system.model.AcquisitionMethod;

public interface AcquisitionMethodRepository extends JpaRepository<AcquisitionMethod, Long> {
	AcquisitionMethod findByDescription(String description);
	AcquisitionMethod findByAcquisitionMethodIdNotAndDescription(Long acquisitionMethodId, String description);
}
