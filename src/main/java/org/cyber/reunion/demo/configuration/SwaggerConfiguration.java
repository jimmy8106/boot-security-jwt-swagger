package org.cyber.reunion.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfiguration {
	private final String moduleName = "demo";
	private final String apiVersion = "1.0.0";

//	public SaggerConfiguration(
//	      @Value("${module-name}") String moduleName,
//	      @Value("${api-version}") String apiVersion) {
//	    this.moduleName = moduleName;
//	    this.apiVersion = apiVersion;
//	  }

	@Bean
	public OpenAPI customOpenAPI() {
		final String securitySchemeName = "bearer-key";
//		final String apiTitle = String.format("%s API", StringUtils.capitalize(moduleName));
		return new OpenAPI().addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
				.components(
						new Components().addSecuritySchemes(securitySchemeName,
								new SecurityScheme().name(securitySchemeName).type(SecurityScheme.Type.HTTP)
										.scheme("bearer").bearerFormat("JWT")))
				.info(new Info().title(moduleName).version(apiVersion));
	}
}
