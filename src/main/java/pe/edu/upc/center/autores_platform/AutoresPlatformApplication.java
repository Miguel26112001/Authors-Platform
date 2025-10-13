package pe.edu.upc.center.autores_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AutoresPlatformApplication {

	public static void main(String[] args) {
    SpringApplication.run(AutoresPlatformApplication.class, args);
	}

}
