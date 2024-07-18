package com.example.demo.controllers;

import java.util.Objects;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.JwtRequest;
import com.example.demo.models.JwtResponse;
import com.example.demo.onfig.JwtTokenUtil;



@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
            throws Exception {

        // Autenticar usando el AuthenticationManager de Spring Security
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        // Cargar detalles del usuario (UserDetails) basados en el nombre de usuario
        final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        // Generar token JWT utilizando el UserDetails cargado
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Devolver el token como parte de la respuesta
        return ResponseEntity.ok(new JwtResponse(token));
    }

    // Método privado para autenticar utilizando AuthenticationManager
    private void authenticate(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            // Se utiliza el AuthenticationManager para autenticar con el nombre de usuario y contraseña
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            // Excepción lanzada si el usuario está deshabilitado en el sistema
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            // Excepción lanzada si las credenciales (usuario/contraseña) no son válidas
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
