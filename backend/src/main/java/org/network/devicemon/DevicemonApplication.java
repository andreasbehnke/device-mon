package org.network.devicemon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DevicemonApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevicemonApplication.class, args);
	}

}
