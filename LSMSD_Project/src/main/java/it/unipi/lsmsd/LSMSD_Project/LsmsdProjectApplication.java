package it.unipi.lsmsd.LSMSD_Project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"it.unipi.lsmsd.LSMSD_Project"})
public class LsmsdProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(LsmsdProjectApplication.class, args);
	}
}
