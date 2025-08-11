package com.nextcloudlab.kickytime.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        String scheme = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("Kickytime API")
                        .version("1.0.0")
                        .description("Kickytime 백엔드 API 명세서"))
                .addSecurityItem(new SecurityRequirement().addList(scheme))
                .components(new Components()
                        .addSecuritySchemes(scheme, new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

}
