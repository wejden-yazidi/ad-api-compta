package com.adcaisse.compta.config;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;


@Configuration
@EnableSwagger2
public class SwaggerConfig {



    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String DEFAULT_INCLUDE_PATTERN = "/v1/.*";

    @Bean
    public Docket customDocket() {

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(regex(DEFAULT_INCLUDE_PATTERN))
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(Lists.newArrayList(apiKey()));

    }

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Bprice Api Customer")
                .description("Bprice ApiCustomer to Manage all operations related to a Bprice customer")
                .license("@CopyRight 2020")
                .licenseUrl("licenseUrl")
                .termsOfServiceUrl("")
                .version("V0.1")
                .contact(new Contact("hassine elarbi","http://51.178.47.149:8090/bp-api-customer/swagger-ui.html#/", "hassine.elarbi@gmail.com"))
                .build();
    }


    private ApiKey apiKey() {
        return new ApiKey("Bearer", AUTHORIZATION_HEADER, "header");
    }


}
