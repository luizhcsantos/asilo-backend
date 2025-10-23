package br.unesp.asilobackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Habilita o CORS usando a configuração da WebConfig
                .cors(withDefaults())

                // 2. Desabilita o CSRF (ESSENCIAL para APIs REST)
                .csrf(csrf -> csrf.disable())

                // 3. Define a política de sessão como STATELESS (não guarda estado)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. Configura as permissões de requisição
                .authorizeHttpRequests(authorize -> authorize
                        // 4a. Permite TODAS as requisições OPTIONS (para o "preflight" do CORS)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 4b. Permite os endpoints públicos de cadastro e login
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/doador/pf").permitAll()
                        .requestMatchers("/api/doador/pj").permitAll()

                        // 4c. Exige autenticação para todas as outras requisições
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
