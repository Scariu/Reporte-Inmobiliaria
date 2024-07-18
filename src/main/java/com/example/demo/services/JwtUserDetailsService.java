package com.example.demo.services;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // En este método se carga el UserDetails basado en el nombre de usuario proporcionado

        // Ejemplo simple: si el nombre de usuario es "javainuse", se retorna un UserDetails con ese nombre de usuario y una contraseña encriptada
        if ("javainuse".equals(username)) {
            return new User("javainuse", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
                    new ArrayList<>()); // La contraseña encriptada es "password"
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

}
