package br.unesp.asilobackend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.unesp.asilobackend.domain.Administrador;
import br.unesp.asilobackend.repository.AdministradorRepository;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedAdmin(AdministradorRepository adminRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            String seedEmail = "admin@example.com";
            if (adminRepo.buscarPorEmail(seedEmail).isEmpty()) {
                Administrador admin = new Administrador();
                admin.setNome("Administrador");
                admin.setEmail(seedEmail);
                admin.setSenhaHash(passwordEncoder.encode("admin123"));
                adminRepo.salvar(admin);
                System.out.println("[DataSeeder] Administrador inicial criado: " + seedEmail + " / admin123");
            }
        };
    }
}
