package com.banking.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Banking API")
                        .version("v1.0")
                        .description("""
                    API documentation for the banking application.
                    
                    **IBANs available in the system:**
                    - DE74500105172121713619
                    - DE50500105179598831884
                    - DE68500105174869319237
                    """)
                )
                .components(new Components()
                        .addSecuritySchemes("oauth2",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.OAUTH2)
                                        .flows(new OAuthFlows()
                                                .clientCredentials(new OAuthFlow()
                                                        .tokenUrl("http://localhost:8080/oauth2/token") // URL для получения токена
                                                        .scopes(new Scopes().addString("read", "Read access"))
                                                )
                                        )
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("oauth2"));
    }
}
