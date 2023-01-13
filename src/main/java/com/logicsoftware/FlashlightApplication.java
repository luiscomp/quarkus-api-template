package com.logicsoftware;

import javax.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(
        tags = {
                @Tag(name="Users", description="Users endpoints to integration.")
        },
        info = @Info(
                title="Flashlight API",
                version = "1.0",
                description = "Application to manage Flashlight Integration",
                contact = @Contact(
                        name = "Logic Software",
                        url = "https://www.logicsoftware.com.br",
                        email = "logicsoftware86@gmail.com"),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html")),
        security = @SecurityRequirement(name = "Keycloak")
)
public class FlashlightApplication extends Application {
}
