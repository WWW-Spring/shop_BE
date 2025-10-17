package com.iuh.fit.se.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
	@Bean
	public OpenAPI springShopOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("Shop Online API")
						.description("API documentation")
						.version("v1.0")
						.license(new License().name("MIT")))
				.servers(List.of(new Server().url("http://localhost:8081")))
				.externalDocs(new ExternalDocumentation().description("Project").url("http://localhost:8081"));
	}
}


