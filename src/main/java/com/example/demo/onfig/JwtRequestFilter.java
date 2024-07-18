package com.example.demo.onfig;

import java.io.IOException;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.demo.services.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Obtenemos el encabezado "Authorization" de la solicitud
        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        // El token JWT está en la forma "Bearer token". Removemos la palabra "Bearer" y obtenemos solo el token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                // Obtenemos el nombre de usuario desde el token JWT
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else {
            // Si el encabezado no comienza con "Bearer", emitimos una advertencia
            logger.warn("JWT Token does not begin with Bearer String");
        }

        // Una vez que obtenemos el nombre de usuario del token, validamos el token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Cargamos los detalles del usuario utilizando el servicio de detalles de usuario JWT
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            // Si el token es válido, configuramos manualmente la autenticación en Spring Security
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                // Creamos un objeto de autenticación usando el nombre de usuario, los detalles del usuario y los roles/autoridades
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Establecemos los detalles adicionales como la fuente de autenticación web (WebAuthenticationDetails)
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Configuramos la autenticación en el contexto de seguridad de Spring
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        // Continuamos la cadena de filtros
        chain.doFilter(request, response);
    }

}
