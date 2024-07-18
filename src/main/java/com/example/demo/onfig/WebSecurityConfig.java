package com.example.demo.onfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // Configuración del AuthenticationManager para que sepa de dónde cargar
        // el usuario para las credenciales coincidentes.
        // Utilizamos BCryptPasswordEncoder para encriptar las contraseñas.
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Bean para proporcionar el encoder de contraseñas BCrypt
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // Bean que expone el AuthenticationManager de Spring Security
        // Necesario para autenticar los tokens en el controlador de autenticación
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // Deshabilitamos CSRF (Cross Site Request Forgery) para este ejemplo
        httpSecurity.csrf().disable()
                // No autenticar esta solicitud particular
                .authorizeRequests().antMatchers("/authenticate").permitAll()
                // Todas las demás solicitudes requieren autenticación
                .anyRequest().authenticated().and()
                // Configuramos el manejo de excepciones y el punto de entrada de autenticación JWT
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                // Configuramos la política de gestión de sesiones como stateless (sin estado)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Agregamos un filtro para validar los tokens en cada solicitud
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
