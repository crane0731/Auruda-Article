package com.sw.AurudaArticle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AurudaArticleApplication {

	public static void main(String[] args) {
		SpringApplication.run(AurudaArticleApplication.class, args);
	}

}
