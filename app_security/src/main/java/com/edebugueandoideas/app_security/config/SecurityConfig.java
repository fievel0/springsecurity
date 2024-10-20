package com.edebugueandoideas.app_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //Cualquiera de las dos configuraciones funciona
    //Primera configuración
   /*@Bean
    public SecurityFilterChain securityFilterChain(@org.jetbrains.annotations.NotNull HttpSecurity http) throws Exception {
        http
                //.csrf(csrf -> csrf.disable()) // Si deseas desactivar CSRF, pero no es recomendable en producción
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/v1/index2").permitAll() // Permitir acceso a esta ruta
                        .anyRequest().authenticated() // Cualquier otra solicitud debe estar autenticada
                )
                .formLogin(form -> form
                        .permitAll() // Permitir acceso a la página de inicio de sesión
                )
                .logout(logout -> logout
                        .permitAll() // Permitir acceso a todos a la página de cierre de sesión
                );

        return http.build();
    }*/

    //Segunda configuración
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws  Exception{
        return httpSecurity
                .authorizeHttpRequests( auth -> {
                    auth.requestMatchers("/v1/index2").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin()
                .successHandler(successHandler()) //url a donde se redirige despues de iniciar seción
                    .permitAll()
                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                    .invalidSessionUrl("/login")
                    .maximumSessions(1)
                    .expiredUrl("/login")
                .and()
                .sessionFixation()//otra vulnerabilidad de aplicacciones web cuando se trabaja con sesiones
                    .migrateSession()//para generar diferentes id de sessión y un atacante no pueda hacer daño ahi
                .and()
                .build();
    }

    public AuthenticationSuccessHandler successHandler(){
        return((request, response, authentication) -> {
           response.sendRedirect("/v1/index");

        });
    }
}
