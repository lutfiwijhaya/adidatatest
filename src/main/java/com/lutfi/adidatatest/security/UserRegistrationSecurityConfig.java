package com.lutfi.adidatatest.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
public class UserRegistrationSecurityConfig {
//
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers("/registration/**").permitAll()
                .requestMatchers("/reset-password/**").permitAll()
                .requestMatchers("/ui-reset-password/**").permitAll()
                .requestMatchers("/register/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login") // Halaman login kustom
                .defaultSuccessUrl("/dashboard") // Halaman setelah login berhasil
                .failureUrl("/login?error=true") // Halaman jika login gagal
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login?logout=true") // Halaman setelah logout berhasil
                .permitAll()
                .and()
                .csrf()
                .disable();

        return http.build();



    }

}
