package com.music.merchandisingMS.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@SecurityScheme(
	name = "Bearer Authentication",
	type = SecuritySchemeType.HTTP,
	bearerFormat = "JWT",
	scheme = "bearer"
)
@OpenAPIDefinition(
        info = @Info(title = "Merchandising API", version = "1.0",
        contact = @Contact(name = "Baeldung", url = "https://www.baeldung.com"),
        license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"),
        description = "<h3>Microservices: <a href='${app.api.authms.domain}/swagger-ui/index.html#/'>Authentication API</a> | <a href='${app.api.userms.domain}/swagger-ui/index.html#/'>User API</a> | <a href='${app.api.musicms.domain}/swagger-ui/index.html#/'>Music API</a> | <a href='${app.api.domain}/swagger-ui/index.html#/'>Merchandising API</a></h3>"
        		+ "<p>When login or register successfully to the system on the Authentication API, copy the token from the response and paste it into the green button that says 'Authorize'. This action will give you permission for use the methods with a LOCK on them (as long as the token is valid).</p>"),
        servers = {
        		@Server(url = "${app.api.domain}", description = "Merchandising API")})
public class OpenApiConfiguration {}
