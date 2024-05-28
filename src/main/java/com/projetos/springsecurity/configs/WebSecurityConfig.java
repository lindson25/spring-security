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
// Habilita a segurança baseada em método, permitindo o uso de anotações de segurança.
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private SecurityDatabaseService securityService;

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        // Método que configura o serviço de detalhes do usuário (UserDetailsService) e o codificador de senha
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

    /*
    (Caso o banco de dados fosse na memória, usaríamos esse bloco de código)
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user")
                .password("{noop}user123")
                .roles("USERS")
                .build());
        manager.createUser(User.withUsername("admin")
                .password("{noop}master123")
                .roles("MANAGERS")
                .build());
        return manager;
    }
    */
}
