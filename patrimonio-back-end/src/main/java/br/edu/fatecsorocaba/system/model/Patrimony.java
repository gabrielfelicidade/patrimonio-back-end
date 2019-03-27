package br.edu.fatecsorocaba.system.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "patrimonyId")
public class Patrimony {

	@Id
	@Column(name = "patrimony_id")
	private Long patrimonyId;

	@Column(name = "acquisition_process_id")
	private String acquisitionProcessId;

	@Column(name = "serial_number")
	private String serialNumber;

	private String description;

	@Column(name = "commercial_invoice")
	private String commercialInvoice;

	private String model;

	private String brand;

	@Column(name = "additional_information")
	private String additionalInformation;

	private BigDecimal value;

	@ManyToOne
	@JoinColumn(name = "location_id")
	@JsonIgnoreProperties("patrimonies")
	private Location location;

	@ManyToOne
	@JoinColumn(name = "acquisition_method_id")
	private AcquisitionMethod acquisitionMethod;

	public Long getPatrimonyId() {
		return patrimonyId;
	}

	public void setPatrimonyId(Long patrimonyId) {
		this.patrimonyId = patrimonyId;
	}

	public String getAcquisitionProcessId() {
		return acquisitionProcessId;
	}

	public void setAcquisitionProcessId(String acquisitionProcessId) {
		this.acquisitionProcessId = acquisitionProcessId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCommercialInvoice() {
		return commercialInvoice;
	}

	public void setCommercialInvoice(String commercialInvoice) {
		this.commercialInvoice = commercialInvoice;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public AcquisitionMethod getAcquisitionMethod() {
		return acquisitionMethod;
	}

	public void setAcquisitionMethod(AcquisitionMethod acquisitionMethod) {
		this.acquisitionMethod = acquisitionMethod;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((patrimonyId == null) ? 0 : patrimonyId.hashCode());
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
		Patrimony other = (Patrimony) obj;
		if (patrimonyId == null) {
			if (other.patrimonyId != null)
				return false;
		} else if (!patrimonyId.equals(other.patrimonyId))
			return false;
		return true;
	}

}
