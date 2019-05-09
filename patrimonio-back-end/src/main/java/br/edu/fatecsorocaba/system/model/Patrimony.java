package br.edu.fatecsorocaba.system.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.edu.fatecsorocaba.system.validationInterfaces.OnCreate;
import br.edu.fatecsorocaba.system.validationInterfaces.OnUpdate;

@Entity
public class Patrimony {

	@Id
	@Column(name = "patrimony_id")
	@NotNull(groups = {OnUpdate.class, OnCreate.class}, message = "The field 'patrimony id' cannot be null")
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

	@Digits(groups = {OnUpdate.class, OnCreate.class}, integer = 8, fraction = 2, message = "The field 'value' should contain only two digits after precision")
	@DecimalMin(groups = {OnUpdate.class, OnCreate.class}, value="1.00")
	private BigDecimal value;
	
	@Min(groups = {OnUpdate.class, OnCreate.class}, value=0)
	@Max(groups = {OnUpdate.class, OnCreate.class}, value=2)
	private int status=2;

	@ManyToOne
	@JoinColumn(name = "location_id")
	@JsonIgnoreProperties("patrimonies")
	private Location location;

	@ManyToOne
	@JoinColumn(name = "acquisition_method_id")
	@JsonIgnoreProperties("patrimonies")
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
