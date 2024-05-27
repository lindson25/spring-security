package com.projetos.springsecurity.configs;

import com.projetos.springsecurity.services.SecurityDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private SecurityDatabaseService securityService;

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/").permitAll() // Permite acesso público à raiz ("/")
                        .requestMatchers("/login").permitAll() // Permite acesso público à página de login
                        .requestMatchers("/managers").hasAnyRole("MANAGERS") // Restringe o acesso a "/managers" para usuários com papel "MANAGERS"
                        .requestMatchers("/users").hasAnyRole("USERS", "MANAGERS") // Restringe o acesso a "/users" para usuários com papel "USERS" ou "MANAGERS"
                        .anyRequest().authenticated() // Qualquer outra requisição deve ser autenticada
                )
                .httpBasic(Customizer.withDefaults()); // Habilita a autenticação HTTP básica com configuração padrão
        // (Caso for por tela de login ->) .formLogin(Customizer.withDefaults()); // Habilita a autenticação via formulário de login padrão
        return http.build();
    }
}
