package Auction.service;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableJpaAuditing
public class ServiceApplication {

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "classpath:application.yaml,"
			+ "classpath:aws.yaml";

	public static void main(String[] args) {
		new SpringApplicationBuilder(ServiceApplication.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args);
	}
}