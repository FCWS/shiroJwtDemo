package com.smkj.shiroAndJwt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("com.smkj.shiroAndJwt.dao")
@SpringBootApplication
public class ShiroAndJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShiroAndJwtApplication.class, args);
	}

}
