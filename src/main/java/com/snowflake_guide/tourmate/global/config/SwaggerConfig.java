package com.snowflake_guide.tourmate.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String jwtSchemeName = "JWT TOKEN";

        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        // Note: Remove securityRequirement and .addSecurityItem if you want to disable the login screen
        return new OpenAPI()
                .info(new Info()
                        .version("v1.0")
                        .title("TourMate API")
                        .description("외국인을 위한 서울 여행 가이드 애플리케이션"))
                .addServersItem(new Server().url("/"))
                .components(components);
    }
}
