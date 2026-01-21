package com.company.ordermanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI orderPaymentInvoiceApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Order Payment Invoice API")
                        .description("Backend system for Order, Payment and Invoice management")
                        .version("v1.0"));
    }
}
