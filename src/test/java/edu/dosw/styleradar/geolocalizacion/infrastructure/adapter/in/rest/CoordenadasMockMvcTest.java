package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.geolocalizacion.domain.model.Almacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.Distancia;
import edu.dosw.styleradar.geolocalizacion.domain.model.Fundacion;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.CalcularDistanciaUseCase;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.ConsultarAlmacenesUseCase;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.ConsultarFundacionesCercanasUseCase;
import edu.dosw.styleradar.geolocalizacion.infrastructure.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({AlmacenController.class, DistanciaController.class, FundacionController.class})
@Import({GlobalExceptionHandler.class, CorrelationIdFilter.class})
class CoordenadasMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConsultarAlmacenesUseCase consultarAlmacenes;

    @MockBean
    private CalcularDistanciaUseCase calcularDistancia;

    @MockBean
    private ConsultarFundacionesCercanasUseCase consultarFundaciones;

    @Test
    void soportaAliasDeCoordenadasYCorrelationId() throws Exception {
        UUID id = UUID.randomUUID();
        when(consultarAlmacenes.consultar(null, 4.66, -74.05, 5.0))
                .thenReturn(List.of(new Almacen(id, "Calle 85", null, new Ubicacion(4.66, -74.05), "900555777-1")));
        when(calcularDistancia.calcular(id, new Ubicacion(4.66, -74.05)))
                .thenReturn(new Distancia(2.5));
        when(consultarFundaciones.consultar(new Ubicacion(4.66, -74.05), new Distancia(5.0)))
                .thenReturn(List.of(new Fundacion(id, "Abrigo Norte", new Ubicacion(4.66, -74.05))));

        mockMvc.perform(get("/stores/nearby")
                        .header("X-Request-Id", "req-geo-1")
                        .param("latitud", "4.66")
                        .param("longitud", "-74.05"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Correlation-Id", "req-geo-1"))
                .andExpect(jsonPath("$[0].nombre").value("Calle 85"));

        mockMvc.perform(get("/stores/" + id + "/distance")
                        .param("latitud", "4.66")
                        .param("lon", "-74.05"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Correlation-Id"))
                .andExpect(jsonPath("$.kilometros").value(2.5));

        mockMvc.perform(get("/foundations/nearby")
                        .param("lat", "4.66")
                        .param("lon", "-74.05"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Abrigo Norte"));
    }

    @Test
    void responde400ConParIncompletoY405ConMetodoNoSoportado() throws Exception {
        mockMvc.perform(get("/stores/nearby").param("latitud", "4.66"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("PARAMETRO_INVALIDO"));

        mockMvc.perform(get("/stores/" + UUID.randomUUID() + "/distance").param("lat", "4.66"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("PARAMETRO_INVALIDO"));

        mockMvc.perform(get("/foundations/nearby"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("PARAMETRO_INVALIDO"));

        mockMvc.perform(post("/stores/nearby"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.code").value("METODO_NO_PERMITIDO"));
    }
}
