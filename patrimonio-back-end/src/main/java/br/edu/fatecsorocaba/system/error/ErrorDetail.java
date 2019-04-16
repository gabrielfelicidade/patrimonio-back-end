package br.edu.fatecsorocaba.system.error;

public class ErrorDetail {
	private String title;
	private int status;
	private String detail;
	private long timestamp;
	private String developerMessage;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getDeveloperMessage() {
		return developerMessage;
	}
	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}
	public ErrorDetail(String title, int status, String detail, long timestamp, String developerMessage) {
		this.title = title;
		this.status = status;
		this.detail = detail;
		this.timestamp = timestamp;
		this.developerMessage = developerMessage;
	}
}
