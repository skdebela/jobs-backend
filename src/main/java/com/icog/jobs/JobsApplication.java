package com.icog.jobs;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JobsApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		dotenv.entries().forEach(dotenvEntry ->
				System.setProperty(dotenvEntry.getKey(), dotenvEntry.getValue())
		);

		SpringApplication.run(JobsApplication.class, args);
	}

}
