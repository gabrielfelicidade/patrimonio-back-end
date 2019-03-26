package br.edu.fatecsorocaba.system.error;

public class CustomError {

	private String error;

	public CustomError(String error) {
		super();
		this.error = error;
	}

	public String getError() {
		return "Error: " + error;
	}

}
