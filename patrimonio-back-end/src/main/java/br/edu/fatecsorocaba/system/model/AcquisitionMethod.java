package br.edu.fatecsorocaba.system.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.edu.fatecsorocaba.system.validationInterfaces.OnCreate;
import br.edu.fatecsorocaba.system.validationInterfaces.OnUpdate;

@Entity(name = "acquisitionmethod")
@Table(name = "acquisitionmethod")
public class AcquisitionMethod {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "acquisition_method_id")
	@NotNull(groups = OnUpdate.class)
	private Long acquisitionMethodId;
	
	@NotEmpty(groups = {OnUpdate.class, OnCreate.class}, message = "The field 'description' cannot be null")
	private String description;

	public Long getAcquisitionMethodId() {
		return acquisitionMethodId;
	}

	public void setAcquisitionMethodId(Long acquisitionMethodId) {
		this.acquisitionMethodId = acquisitionMethodId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acquisitionMethodId == null) ? 0 : acquisitionMethodId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AcquisitionMethod other = (AcquisitionMethod) obj;
		if (acquisitionMethodId == null) {
			if (other.acquisitionMethodId != null)
				return false;
		} else if (!acquisitionMethodId.equals(other.acquisitionMethodId))
			return false;
		return true;
	}

}
