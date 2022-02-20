package io.github.template.configuration

import io.github.wickedev.spring.reactive.security.DslRoleHierarchy
import io.github.wickedev.spring.reactive.security.decoder.JwtDecoder
import io.github.wickedev.spring.reactive.security.jwt.JwtAuthenticationWebFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
class SecurityConfiguration : WebFluxConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH")
    }

    @Bean
    fun roleHierarchy(): RoleHierarchy = DslRoleHierarchy {
        "ROLE_ADMIN" {
            "ROLE_MANAGER" {
                +"ROLE_USER"
            }
        }
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource =
        UrlBasedCorsConfigurationSource().apply {
            val configuration = CorsConfiguration()
            configuration.allowedOriginPatterns = listOf("*")
            configuration.allowedMethods = listOf("HEAD", "GET", "POST", "PUT", "DELETE")
            configuration.allowCredentials = true
            configuration.allowedHeaders = listOf("*")
            registerCorsConfiguration("/**", configuration)
        }

    @Bean
    fun configure(
        http: ServerHttpSecurity,
        jwtDecoder: JwtDecoder,
        corsConfigurationSource: CorsConfigurationSource
    ): SecurityWebFilterChain {
        http.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        return http {
            cors {
                configurationSource = corsConfigurationSource
            }
            csrf { disable() }
            httpBasic { disable() }
            formLogin { disable() }
            logout { disable() }
            authorizeExchange {
                authorize("/**", permitAll)
            }
            addFilterAt(JwtAuthenticationWebFilter(jwtDecoder), SecurityWebFiltersOrder.AUTHENTICATION)
        }
    }
}
