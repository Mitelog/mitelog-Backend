package kr.co.ync.projectA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = "kr.co.ync")
@EnableJpaAuditing
public class ProjectAApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectAApplication.class, args);
	}

}
