package com.projetos.springsecurity.services;

import com.projetos.springsecurity.models.User;
import com.projetos.springsecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Classe responsável por carregar os detalhes do usuário (UserDetails) a partir
 * de um repositório de dados com base no nome de usuário fornecido.
 *
 * Objetivo Principal:
 * - Integrar a aplicação com o Spring Security para realizar a autenticação e
 *   autorização de usuários.
 */

@Service
public class SecurityDatabaseService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {

        // Busca o usuário no repositório pelo nome de usuário
        User userEntity = userRepository.findByUsername(username);

        // Se o usuário não for encontrado, lança uma exceção
        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        // Cria um conjunto para armazenar as autoridades (permissões) do usuário
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

        // Itera sobre os papéis (roles) do usuário e adiciona cada um como uma autoridade
        userEntity.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        });

        // Cria um objeto UserDetails com o nome de usuário, senha e autoridades do usuário
        UserDetails user = new org.springframework.security.core.userdetails.User(userEntity.getUsername(),
                userEntity.getPassword(),
                authorities);

        // Retorna o objeto UserDetails
        return user;
    }
}
