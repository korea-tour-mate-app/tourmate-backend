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
                        .in(SecurityScheme.In.HEADER)           // HTTP 헤더에 포함됨을 지정
                        .bearerFormat("JWT"));

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        return new OpenAPI()
                .info(new Info()
                        .version("v1.0")
                        .title("TourMate API")
                        .description("외국인을 위한 서울 여행 가이드 애플리케이션"))
                .addServersItem(new Server().url("/"))
                .components(components)
                .addSecurityItem(securityRequirement);  // 이 줄을 추가하여 JWT 인증을 모든 요청에 자동으로 추가
    }
}