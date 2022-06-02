package org.cyber.reunion.demo.configuration;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

@Configuration
public class SwaggerConfiguration {
    public static final String AUTHORIZATION_HEADER = "Authorization";

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
        		.title("JSON Web Token Authentication API")
                .description("This is a sample JWT authentication service. You can find out more about JWT at [https://jwt.io/](https://jwt.io/). For this sample, you can use the `admin` or `client` users (password: admin and client respectively) to test the authorization filters. Once you have successfully logged in and obtained the token, you should click on the right top button `Authorize` and introduce it with the prefix \"Bearer \".")
                .version("1.0.0")
                .license("MIT License").licenseUrl("http://opensource.org/licenses/MIT")//
                .contact(new Contact(null, null, "loktar8106@gmail.com"))
                .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.cyber.reunion.demo.rest"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey(AUTHORIZATION_HEADER, "JWT", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference(AUTHORIZATION_HEADER, authorizationScopes));
    }
    
	// fixing spirng boot 2.6 with swagger
 	@Bean
 	public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
 		return new BeanPostProcessor() {

 			@Override
 			public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
 				if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
 					customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
 				}
 				return bean;
 			}

 			private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(
 					List<T> mappings) {
 				List<T> copy = mappings.stream().filter(mapping -> mapping.getPatternParser() == null)
 						.collect(Collectors.toList());
 				mappings.clear();
 				mappings.addAll(copy);
 			}

 			@SuppressWarnings("unchecked")
 			private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
 				try {
 					Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
 					field.setAccessible(true);
 					return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
 				} catch (IllegalArgumentException | IllegalAccessException e) {
 					throw new IllegalStateException(e);
 				}
 			}
 		};
 	}
}
