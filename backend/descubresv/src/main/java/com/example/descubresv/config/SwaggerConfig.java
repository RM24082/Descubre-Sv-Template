package com.example.descubresv.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

// Configuracion de Swagger/OpenAPI - documentacion de la API
@Configuration
public class SwaggerConfig {

        @Bean
        public OpenAPI descubreSvOpenAPI() {
                // Nombre del esquema de seguridad JWT
                final String esquemaSeguridad = "Bearer JWT";

                return new OpenAPI()
                                // Informacion general de la API
                                .info(new Info()
                                                .title("DescubreSV API")
                                                .description("API RESTful para la plataforma de turismo en El Salvador. "
                                                                + "Permite a los turistas explorar destinos, crear itinerarios, "
                                                                + "calcular presupuestos y dejar resenas de sus experiencias.")
                                                .version("1.0.0")
                                                .contact(new Contact()
                                                                .name("Equipo DescubreSV")
                                                                .email("contacto@descubresv.com"))
                                                .license(new License()
                                                                .name("Uso Academico - DAW135")
                                                                .url("https://github.com/descubresv")))

                                // Esquema de seguridad JWT Bearer
                                .components(new Components()
                                                .addSecuritySchemes(esquemaSeguridad, new SecurityScheme()
                                                                .name(esquemaSeguridad)
                                                                .type(SecurityScheme.Type.HTTP)
                                                                .scheme("bearer")
                                                                .bearerFormat("JWT")
                                                                .description("Ingrese el token JWT. Se obtiene al iniciar sesion en /api/auth/login")))

                                // Requerir JWT en todos los endpoints por defecto
                                .addSecurityItem(new SecurityRequirement().addList(esquemaSeguridad))

                                // Tags en espanol para agrupar los endpoints
                                .tags(List.of(
                                                new Tag().name("Autenticacion")
                                                                .description("Registro e inicio de sesion de usuarios"),
                                                new Tag().name("Destinos")
                                                                .description("Consulta y gestion de destinos turisticos"),
                                                new Tag().name("Categorias")
                                                                .description("Categorias de destinos turisticos"),
                                                new Tag().name("Favoritos")
                                                                .description("Gestion de destinos favoritos del turista"),
                                                new Tag().name("Resenas")
                                                                .description("Resenas y calificaciones de destinos"),
                                                new Tag().name("Itinerarios")
                                                                .description("Planificacion de itinerarios de viaje"),
                                                new Tag().name("Presupuestos")
                                                                .description("Calculo y gestion de presupuestos de viaje"),
                                                new Tag().name("Transportes")
                                                                .description("Opciones de transporte por destino"),
                                                new Tag().name("Alimentacion")
                                                                .description("Opciones de alimentacion por destino"),
                                                new Tag().name("Admin - Destinos")
                                                                .description("Gestion administrativa de destinos"),
                                                new Tag().name("Admin - Usuarios")
                                                                .description("Gestion administrativa de usuarios")));
        }
}
