package edu.dosw.styleradar.geolocalizacion;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
class GeolocalizacionIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @Container
    static final RabbitMQContainer RABBIT = new RabbitMQContainer("rabbitmq:3.13.7-management-alpine");

    private static final String EXCHANGE = "styleradar.exchange";

    @DynamicPropertySource
    static void datasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.flyway.locations", () -> "classpath:db/migration,classpath:db/seed");
        registry.add("spring.rabbitmq.host", RABBIT::getHost);
        registry.add("spring.rabbitmq.port", RABBIT::getAmqpPort);
        registry.add("spring.rabbitmq.username", RABBIT::getAdminUsername);
        registry.add("spring.rabbitmq.password", RABBIT::getAdminPassword);
        registry.add("spring.rabbitmq.listener.simple.auto-startup", () -> "true");
        registry.add("tiendas.carga.habilitada", () -> "false");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void listaLosAlmacenesDeLaBase() throws Exception {
        mockMvc.perform(get("/almacenes").param("lat", "4.6762").param("lng", "-74.0481").param("radioKm", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].nombre", hasItem("Calle 85 Store")));
    }

    @Test
    void filtraAlmacenesPorCategoria() throws Exception {
        mockMvc.perform(get("/almacenes").param("lat", "4.6762").param("lng", "-74.0481").param("categoriaId", "22222222-2222-2222-2222-222222222222"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoriaId").value("22222222-2222-2222-2222-222222222222"))
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
                .andExpect(jsonPath("$[0].nombre").value("Fundación Abrigo Norte"))
                .andExpect(jsonPath("$[0].distanciaKm").isNumber());
    }

    @Test
    void guardaElAlmacenQuePublicaTiendas() throws Exception {
        String id = "a9999999-9999-9999-9999-999999999999";
        rabbitTemplate.convertAndSend(EXCHANGE, "styleradar.tiendas.almacen.registrado", Map.of("almacenId", id,
                "nombre", "Almacén de prueba", "categoria", "Deportivo", "latitud", 6.2442, "longitud", -75.5812));

        int estado = 404;
        for (int i = 0; i < 50 && estado != 200; i++) {
            Thread.sleep(100);
            estado = mockMvc.perform(get("/almacenes/" + id + "/distancia").param("lat", "6.2442").param("lng", "-75.5812"))
                    .andReturn().getResponse().getStatus();
        }

        assertThat(estado).isEqualTo(200);
    }

    @Test
    void guardaLaFundacionQuePublicaTiendas() throws Exception {
        rabbitTemplate.convertAndSend(EXCHANGE, "styleradar.tiendas.fundacion.registrada", Map.of(
                "fundacionId", "f9999999-9999-9999-9999-999999999999", "nombre", "Ropero Medellín",
                "latitud", 6.2442, "longitud", -75.5812));

        String body = "[]";
        for (int i = 0; i < 50 && !body.contains("Ropero"); i++) {
            Thread.sleep(100);
            body = mockMvc.perform(get("/fundaciones/cercanas").param("lat", "6.2442").param("lng", "-75.5812"))
                    .andReturn().getResponse().getContentAsString();
        }

        assertThat(body).contains("Ropero");
    }

    @Test
    void unEventoInvalidoTerminaEnLaColaDeErrores() {
        rabbitTemplate.convertAndSend(EXCHANGE, "styleradar.tiendas.almacen.actualizado", Map.of("almacenId", "no-es-uuid"));

        Object mensaje = rabbitTemplate.receiveAndConvert("styleradar.geolocalizacion.dlq", 10000);
        assertThat(mensaje).isInstanceOf(Map.class);
    }

    @Test
    void existenLasCategoriasDelAnalisis() throws Exception {
        mockMvc.perform(get("/almacenes").param("lat", "4.6762").param("lng", "-74.0481").param("categoriaId", "66666666-6666-6666-6666-666666666666"))
                .andExpect(status().isOk());
    }

    @Test
    void responde400SiLaUbicacionOElRadioNoSonValidos() throws Exception {
        mockMvc.perform(get("/almacenes").param("lng", "-74.0"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/almacenes").param("lat", "95").param("lng", "-74.0"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/almacenes").param("lat", "4.6").param("lng", "-74.0").param("radioKm", "25"))
                .andExpect(status().isBadRequest());
    }
}
