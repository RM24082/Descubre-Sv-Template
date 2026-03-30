package com.example.descubresv.config;

import com.example.descubresv.model.entity.Usuario;
import com.example.descubresv.model.enums.RolUsuario;
import com.example.descubresv.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner initData(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                // Generar los usuarios administradores
                Iterable<Usuario> admins = List.of(
                        Usuario.builder()
                                .nombre("José Wilfredo Ponce Barahona")
                                .correo("pb24007@ues.edu.sv")
                                .passwordHash(passwordEncoder.encode("SuperUser-2026!"))
                                .rol(RolUsuario.ADMIN)
                                .activo(true)
                                .build(),
                        Usuario.builder()
                                .nombre("Ana Estefany Quintanilla de Ponce")
                                .correo("qp24002@ues.edu.sv")
                                .passwordHash(passwordEncoder.encode("SuperUser-2026!"))
                                .rol(RolUsuario.ADMIN)
                                .activo(true)
                                .build(),
                        Usuario.builder()
                                .nombre("Daniel Alexis Ramirez Martinez")
                                .correo("rm24082@ues.edu.sv")
                                .passwordHash(passwordEncoder.encode("SuperUser-2026!"))
                                .rol(RolUsuario.ADMIN)
                                .activo(true)
                                .build(),
                        Usuario.builder()
                                .nombre("Javier Alexander Rodriguez Flores")
                                .correo("rf24006@ues.edu.sv")
                                .passwordHash(passwordEncoder.encode("SuperUser-2026!"))
                                .rol(RolUsuario.ADMIN)
                                .activo(true)
                                .build(),
                        Usuario.builder()
                                .nombre("Eileen Marisol Reyes Rodriguez")
                                .correo("rr24044@ues.edu.sv")
                                .passwordHash(passwordEncoder.encode("SuperUser-2026!"))
                                .rol(RolUsuario.ADMIN)
                                .activo(true)
                                .build());

                usuarioRepository.saveAll(admins);
                System.out.println(" [DESCUBRE-SV] Administradores creados exitosamente (Seeder).");
            } else {
                System.out.println(" [DESCUBRE-SV] La base de datos ya contiene usuarios, no se ejecutara el seeder.");
            }
        };
    }
}
