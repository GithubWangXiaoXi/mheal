package com.wangxiaoxi.mheal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching
@EnableTransactionManagement
public class MhealApplication {

	public static void main(String[] args) {
		SpringApplication.run(MhealApplication.class, args);
	}

}
