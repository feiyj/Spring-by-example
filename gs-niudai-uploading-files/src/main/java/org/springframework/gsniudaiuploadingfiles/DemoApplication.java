package org.springframework.gsniudaiuploadingfiles;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//需要加上@EnableConfigurationProperties, 这样才能让其起效
@EnableConfigurationProperties(StoreProperties.class)

public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	/**
	 * 将storageService初始化.
	 */
	@Bean
	CommandLineRunner init(StoreService storagService) {
		return (args) -> {
			storagService.deleteAll();
			storagService.init();
		};
	}
}

