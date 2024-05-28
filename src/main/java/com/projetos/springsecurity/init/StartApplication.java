package com.projetos.springsecurity.init;

import com.projetos.springsecurity.models.User;
import com.projetos.springsecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StartApplication implements CommandLineRunner {

    @Autowired
    private UserRepository repository;

    @Transactional
    @Override
    public void run(String... args) throws Exception {

        // Busca por um usuário com nome de usuário "admin"
        User user = repository.findByUsername("admin");

        // Se não houver usuário "admin", cria um novo
        if (user == null) {
            user = new User();
            user.setName("ADMIN");
            user.setUsername("admin");
            user.setPassword("master123");
            user.getRoles().add("MANAGERS");
            repository.save(user);
        }

        // Busca por um usuário com nome de usuário "user"
        user = repository.findByUsername("user");

        // Se não houver usuário "user", cria um novo
        if (user == null) {
            user = new User();
            user.setName("USER");
            user.setUsername("user");
            user.setPassword("user123");
            user.getRoles().add("USERS");
            repository.save(user);
        }
    }
}
