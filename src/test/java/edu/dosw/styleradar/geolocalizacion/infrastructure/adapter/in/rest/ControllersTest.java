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
import edu.dosw.styleradar.geolocalizacion.domain.exception.ParametroInvalidoException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ControllersTest {

    @Test
    void listaLosAlmacenes() {
        // Arrange
        ConsultarAlmacenesUseCase useCase = mock(ConsultarAlmacenesUseCase.class);
        Almacen almacen = new Almacen(UUID.randomUUID(), "Calle 85", null, new Ubicacion(4.66, -74.05), "900555777-1");
        when(useCase.consultar(null, 4.66, -74.05, 5)).thenReturn(List.of(almacen));

        // Act
        ResponseEntity<List<AlmacenResponse>> response = new AlmacenController(useCase).listar(4.66, -74.05, null, null, null, 5, null);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).extracting(AlmacenResponse::nombre).containsExactly("Calle 85");
        assertThat(response.getBody()).extracting(AlmacenResponse::nit).containsExactly("900555777-1");
        assertThat(response.getBody()).extracting(AlmacenResponse::distanciaKm).containsExactly(0.0);
    }

    @Test
    void calculaLaDistancia() {
        // Arrange
        CalcularDistanciaUseCase useCase = mock(CalcularDistanciaUseCase.class);
        UUID id = UUID.randomUUID();
        when(useCase.calcular(id, new Ubicacion(4.78, -74.04))).thenReturn(new Distancia(3.456));

        // Act
        ResponseEntity<DistanciaResponse> response = new DistanciaController(useCase).calcular(id, 4.78, -74.04, null, null, null);

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
        ResponseEntity<List<FundacionResponse>> response = new FundacionController(useCase).cercanas(4.78, -74.04, null, null, null, 5);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).extracting(FundacionResponse::nombre).containsExactly("Abrigo Norte");
        assertThat(response.getBody()).extracting(FundacionResponse::distanciaKm).containsExactly(0.0);
    }

    @Test
    void sinUbicacionListaTodosSinDistanciaYConSoloUnaCoordenadaEsInvalido() {
        ConsultarAlmacenesUseCase useCase = mock(ConsultarAlmacenesUseCase.class);
        Almacen almacen = new Almacen(UUID.randomUUID(), "Calle 85", null, new Ubicacion(4.66, -74.05), "900555777-1");
        when(useCase.todos(null)).thenReturn(List.of(almacen));
        AlmacenController controller = new AlmacenController(useCase);

        assertThat(controller.listar(null, null, null, null, null, 5, null).getBody())
                .extracting(AlmacenResponse::distanciaKm, AlmacenResponse::nit)
                .containsExactly(tuple(null, "900555777-1"));
        assertThatThrownBy(() -> controller.listar(4.66, null, null, null, null, 5, null)).isInstanceOf(ParametroInvalidoException.class);
    }

    @Test
    void soportaAliasLatitudLongitudYLon() {
        ConsultarAlmacenesUseCase almacenesUseCase = mock(ConsultarAlmacenesUseCase.class);
        when(almacenesUseCase.consultar(null, 4.66, -74.05, 5)).thenReturn(List.of());
        CalcularDistanciaUseCase distanciaUseCase = mock(CalcularDistanciaUseCase.class);
        UUID id = UUID.randomUUID();
        when(distanciaUseCase.calcular(id, new Ubicacion(4.78, -74.04))).thenReturn(new Distancia(1.2));
        ConsultarFundacionesCercanasUseCase fundacionesUseCase = mock(ConsultarFundacionesCercanasUseCase.class);
        when(fundacionesUseCase.consultar(new Ubicacion(4.78, -74.04), new Distancia(5))).thenReturn(List.of());

        assertThat(new AlmacenController(almacenesUseCase).listar(null, null, 4.66, -74.05, null, 5, null).getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(new DistanciaController(distanciaUseCase).calcular(id, null, null, 4.78, null, -74.04).getBody())
                .isEqualTo(new DistanciaResponse(id, 1.2));
        assertThat(new FundacionController(fundacionesUseCase).cercanas(null, null, 4.78, -74.04, null, 5).getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThatThrownBy(() -> new DistanciaController(distanciaUseCase).calcular(id, null, null, null, null, null))
                .isInstanceOf(ParametroInvalidoException.class);
    }
}
