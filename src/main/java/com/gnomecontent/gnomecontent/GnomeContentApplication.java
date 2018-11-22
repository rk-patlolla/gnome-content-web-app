package com.gnomecontent.gnomecontent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.thymeleaf.dialect.springdata.SpringDataDialect;

@SpringBootApplication
@ComponentScan("com.gnomecontent")
@EntityScan(basePackages = "com.gnomecontent")
@EnableElasticsearchRepositories(basePackages = "com.gnomecontent")
public class GnomeContentApplication {

	public static void main(String[] args) {
		SpringApplication.run(GnomeContentApplication.class, args);
	}

	@Bean
	public SpringDataDialect springDataDialect() {
		return new SpringDataDialect();
	}
}
