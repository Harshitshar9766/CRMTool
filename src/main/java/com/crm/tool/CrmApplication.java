package com.crm.tool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrmApplication {

	public static void main(String[] args) {
		System.out.println("Application Starting...");
		SpringApplication.run(CrmApplication.class, args);
		System.out.println("Application Started... :)");


	}

}
