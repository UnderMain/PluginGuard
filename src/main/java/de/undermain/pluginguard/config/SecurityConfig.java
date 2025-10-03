package de.undermain.pluginguard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //Config Datei
@EnableWebSecurity //Aktiviert Web Security
public class SecurityConfig {

    //Security Filter definiert welche Endpoints geschützt sind
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) //CSRF deaktivieren für REST API
                .authorizeHttpRequests(auth -> auth
                        //Öffentlicher Endpoint - Minecraft Plugins können ohne Login validieren
                        .requestMatchers("/api/v1/licenses/validate").permitAll()

                        //Alle anderen License-Endpoints benötigen Login (Admin)
                        .requestMatchers("/api/v1/licenses/**").authenticated()

                        //Alles andere auch mit Login
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> {}); //HTTP Basic Authentication (Username:Passwort)

        return http.build();
    }

    //Benutzer-Service - definiert wer sich einloggen kann
    @Bean
    public UserDetailsService userDetailsService() {
        //Admin-Benutzer erstellen
        UserDetails admin = User.builder()
                .username("admin") //Benutzername: admin
                .password(passwordEncoder().encode("passwort"))
                .roles("ADMIN") //Rolle: ADMIN
                .build();

        //Benutzer im Speicher halten (nicht in Datenbank)
        return new InMemoryUserDetailsManager(admin);
    }

    //Passwort-Verschlüsselung
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //Sichere Verschlüsselung
    }

}