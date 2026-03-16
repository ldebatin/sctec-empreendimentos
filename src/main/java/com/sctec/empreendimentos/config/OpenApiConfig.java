package com.sctec.empreendimentos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SCTEC Empreendimentos API")
                        .description("API para gerenciamento de empreendimentos de Santa Catarina")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("SCTEC")
                                .email("contato@sctec.com.br")));
    }
}
