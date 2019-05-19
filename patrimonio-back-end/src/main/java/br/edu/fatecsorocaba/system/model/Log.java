package br.edu.fatecsorocaba.system.model;

import java.time.Instant;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Log {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_id")
	private Long logId;

	private Date date;
	
	@Column(name="interfacename")
	private String interfaceName;

	private String action;

	@Column(name = "user_id")
	private String user;

	public Log(String interfaceName, String action, String user) {
		super();
		this.date = Date.from(Instant.now());
		this.interfaceName = interfaceName;
		this.action = action;
		this.user = user;
	}

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user_id) {
		this.user = user_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((logId == null) ? 0 : logId.hashCode());
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
		Log other = (Log) obj;
		if (logId == null) {
			if (other.logId != null)
				return false;
		} else if (!logId.equals(other.logId))
			return false;
		return true;
	}

}
