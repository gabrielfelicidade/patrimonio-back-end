package br.edu.fatecsorocaba.system.error;

public class ResourceNotFoundDetails extends ErrorDetail {

	public ResourceNotFoundDetails(String title, int status, String detail, long timestamp, String developerMessage) {
		super(title, status, detail, timestamp, developerMessage);
	}
}