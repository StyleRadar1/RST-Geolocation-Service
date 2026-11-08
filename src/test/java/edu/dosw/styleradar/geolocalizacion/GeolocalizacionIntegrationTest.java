package edu.dosw.styleradar.geolocalizacion;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Levanta el servicio completo contra un PostgreSQL real con las migraciones de Flyway
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
class GeolocalizacionIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:15.8-alpine");

    @DynamicPropertySource
    static void datasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listaLosAlmacenesDeLaBase() throws Exception {
        mockMvc.perform(get("/almacenes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void filtraAlmacenesPorCategoria() throws Exception {
        mockMvc.perform(get("/almacenes").param("categoriaId", "22222222-2222-2222-2222-222222222222"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Calle 85 Store"));
    }

    @Test
    void calculaLaDistanciaAUnAlmacen() throws Exception {
        mockMvc.perform(get("/almacenes/a1111111-1111-1111-1111-111111111111/distancia")
                        .param("lat", "4.676200").param("lng", "-74.048100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kilometros").value(0.0));
    }

    @Test
    void responde404SiElAlmacenNoExiste() throws Exception {
        mockMvc.perform(get("/almacenes/00000000-0000-0000-0000-000000000000/distancia")
                        .param("lat", "4.6").param("lng", "-74.0"))
                .andExpect(status().isNotFound());
    }

    @Test
    void responde400SiFaltaLaLatitud() throws Exception {
        mockMvc.perform(get("/fundaciones/cercanas").param("lng", "-74.0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listaLasFundacionesCercanas() throws Exception {
        mockMvc.perform(get("/fundaciones/cercanas").param("lat", "4.782700").param("lng", "-74.042700"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Fundación Abrigo Norte"));
    }
}
