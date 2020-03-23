package br.edu.fatecsorocaba.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class PatrimonioBackEndApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(PatrimonioBackEndApplication.class, args);
	}

}
