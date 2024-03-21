package com.example.Api_version.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "FANGBEMI Abla Ilétou",
                        email = "edithfangbemi@gmail.com"
                ),
                description = "OpenApi documentation for spring boot",
                title = "OpenApi specification - Ilétou",
                version = "1.0",
                license = @License(
                        name = "",
                        url =""
                ),
                termsOfService = "terms of services"

        ),
        servers = {
                //liste de serveur
                @Server(
                        description = "Local Environment",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "PROD Environment",
                        url = "http://192.168.20.0:8080"
                )
        },
        security = @SecurityRequirement(
                name = "BearerToken"
        )


)

@SecurityScheme(
        name = "BearerToken",
        type = SecuritySchemeType.HTTP,
        description = "JWT auth description",
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER

)
public class OpenApiConfig {

}
