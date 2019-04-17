package br.edu.fatecsorocaba.system.error;

public class ValidationErrorDetails extends ErrorDetails {
	private String fields;
	private String fieldMessage;
	
	public String getFields() {
		return fields;
	}

	public String getFieldsMessage() {
		return fieldMessage;
	}

	public ValidationErrorDetails(String title, int status, String detail, long timestamp, String developerMessage,
			String fields, String fieldMessage) {
		super(title, status, detail, timestamp, developerMessage);
		this.fields = fields;
		this.fieldMessage = fieldMessage;
	}
}
