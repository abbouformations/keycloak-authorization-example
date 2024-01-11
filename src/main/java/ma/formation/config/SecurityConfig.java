package ma.formation.config;

import lombok.AllArgsConstructor;
import ma.formation.security.CustomAuthenticationEntryPoint;
import ma.formation.security.CustomFilter;
import org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter;
import org.keycloak.adapters.authorization.spi.ConfigurationResolver;
import org.keycloak.adapters.authorization.spi.HttpRequest;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.keycloak.util.JsonSerialization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    //private CustomFilter customFilter;

    //private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                    //    .authenticationEntryPoint(customAuthenticationEntryPoint))
                .addFilterAfter(createPolicyEnforcerFilter(), BearerTokenAuthenticationFilter.class)
                //  .addFilterAfter(customFilter, BearerTokenAuthenticationFilter.class)
                .build();
    }

    private ServletPolicyEnforcerFilter createPolicyEnforcerFilter() {
        return new ServletPolicyEnforcerFilter(new ConfigurationResolver() {
            @Override
            public PolicyEnforcerConfig resolve(HttpRequest request) {
                try {
                    PolicyEnforcerConfig policyEnforcerConfig = JsonSerialization.readValue(getClass().getResourceAsStream("/keycloakPolicy.json"), PolicyEnforcerConfig.class);
                    return policyEnforcerConfig;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
