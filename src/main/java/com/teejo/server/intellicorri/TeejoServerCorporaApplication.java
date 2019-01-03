package com.teejo.server.intellicorri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableSwagger2
public class TeejoServerCorporaApplication extends SpringBootServletInitializer {
	
	public static void main(String[] args) {
		SpringApplication.run(TeejoServerCorporaApplication.class, args);
	}

	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.teejo.server.intellicorri.admin.controller"))
				.paths(PathSelectors.any())
				.build();
	}
	
	private ApiInfo apiInfo() {
		Contact contact = new Contact("teejo", "https://www.teejo.com.cn", "qinhao@teejo.com.cn");
		ApiInfo apiInfo = new ApiInfoBuilder()
				.title("语料库-管理系统")
				.description("语料库-后台管理系统-服务端")
				.version("1.0.0")
				.termsOfServiceUrl("https://www.teejo.com.cn")
				.contact(contact)
				.license("TEEJO ALL RIGHT RESERVED")
				.licenseUrl("https://www.teejo.com.cn")
				.build();
		return apiInfo;
	}

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(TeejoServerCorporaApplication.class);
    }
	
}
