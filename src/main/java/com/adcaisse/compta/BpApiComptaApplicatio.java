package com.adcaisse.compta;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EntityScan(basePackages={"com.bprice.persistance.model"})
@SpringBootApplication

public class BpApiComptaApplicatio {

	public static void main(String[] args) {
		SpringApplication.run(BpApiComptaApplicatio.class, args);
	}
	
}
