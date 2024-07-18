package com.example.demo.models;

import java.io.Serializable;

public class JwtResponse implements Serializable {

    // Identificador único para la serialización de objetos
    private static final long serialVersionUID = -8091879091924046844L;
    
    // Campo que almacena el token JWT
    private final String jwttoken;

    // Constructor que inicializa el objeto JwtResponse con un token JWT
    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    // Método getter para obtener el token JWT almacenado en jwttoken
    public String getToken() {
        return this.jwttoken;
    }
}
