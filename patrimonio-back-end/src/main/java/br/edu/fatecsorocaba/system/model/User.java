package br.edu.fatecsorocaba.system.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.edu.fatecsorocaba.system.validationInterfaces.OnChangePassword;
import br.edu.fatecsorocaba.system.validationInterfaces.OnCreate;
import br.edu.fatecsorocaba.system.validationInterfaces.OnLogin;
import br.edu.fatecsorocaba.system.validationInterfaces.OnUpdate;

@Entity
@Table(name = "[User]")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	@NotNull(groups = OnUpdate.class)
	private Long userId;

	@NotEmpty(groups = {OnUpdate.class, OnCreate.class}, message = "The field 'name' cannot be null")
	private String name;

	@NotEmpty(groups = {OnUpdate.class, OnCreate.class, OnLogin.class}, message = "The field 'username' cannot be null")
	private String username;

	@NotEmpty(groups = {OnCreate.class, OnLogin.class, OnChangePassword.class}, message = "The field 'password' cannot be null")
	private String password;

	@NotNull(groups = {OnUpdate.class, OnCreate.class}, message = "The field 'userlevel' cannot be null")
	@Min(groups = {OnUpdate.class, OnCreate.class}, value=0)
	@Max(groups = {OnUpdate.class, OnCreate.class}, value=2)
	private int userlevel;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}
	
	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	public int getUserlevel() {
		return userlevel;
	}

	public void setUserlevel(int userlevel) {
		this.userlevel = userlevel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		User other = (User) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
