package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.geolocalizacion.application.dto.response.AlmacenResponse;
import edu.dosw.styleradar.geolocalizacion.application.dto.response.DistanciaResponse;
import edu.dosw.styleradar.geolocalizacion.application.dto.response.FundacionResponse;
import edu.dosw.styleradar.geolocalizacion.domain.model.Almacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.Distancia;
import edu.dosw.styleradar.geolocalizacion.domain.model.Fundacion;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.CalcularDistanciaUseCase;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.ConsultarAlmacenesUseCase;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.ConsultarFundacionesCercanasUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ControllersTest {

    @Test
    void listaLosAlmacenes() {
        // Arrange
        ConsultarAlmacenesUseCase useCase = mock(ConsultarAlmacenesUseCase.class);
        Almacen almacen = new Almacen(UUID.randomUUID(), "Calle 85", null, new Ubicacion(4.66, -74.05));
        when(useCase.consultar(null)).thenReturn(List.of(almacen));

        // Act
        ResponseEntity<List<AlmacenResponse>> response = new AlmacenController(useCase).listar(null);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).extracting(AlmacenResponse::nombre).containsExactly("Calle 85");
    }

    @Test
    void calculaLaDistancia() {
        // Arrange
        CalcularDistanciaUseCase useCase = mock(CalcularDistanciaUseCase.class);
        UUID id = UUID.randomUUID();
        when(useCase.calcular(id, new Ubicacion(4.78, -74.04))).thenReturn(new Distancia(3.456));

        // Act
        ResponseEntity<DistanciaResponse> response = new DistanciaController(useCase).calcular(id, 4.78, -74.04);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(new DistanciaResponse(id, 3.46));
    }

    @Test
    void listaLasFundacionesCercanas() {
        // Arrange
        ConsultarFundacionesCercanasUseCase useCase = mock(ConsultarFundacionesCercanasUseCase.class);
        Fundacion fundacion = new Fundacion(UUID.randomUUID(), "Abrigo Norte", new Ubicacion(4.78, -74.04));
        when(useCase.consultar(any(), any())).thenReturn(List.of(fundacion));

        // Act
        ResponseEntity<List<FundacionResponse>> response = new FundacionController(useCase).cercanas(4.78, -74.04, 5);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).extracting(FundacionResponse::nombre).containsExactly("Abrigo Norte");
    }
}
