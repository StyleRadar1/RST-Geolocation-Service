package edu.dosw.styleradar.geolocalizacion.application.usecase;

import edu.dosw.styleradar.geolocalizacion.domain.exception.ResourceNotFoundException;
import edu.dosw.styleradar.geolocalizacion.domain.model.Almacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.Distancia;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.AlmacenRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CalcularDistanciaUseCaseImplTest {

    private final AlmacenRepositoryPort repository = mock(AlmacenRepositoryPort.class);
    private final CalcularDistanciaUseCaseImpl useCase = new CalcularDistanciaUseCaseImpl(repository);

    @Test
    void calculaLaDistanciaAlAlmacen() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(new Almacen(id, "Calle 85", null, new Ubicacion(0, 1))));

        // Act
        Distancia distancia = useCase.calcular(id, new Ubicacion(0, 0));

        // Assert
        assertThat(distancia.kilometros()).isCloseTo(111.19, within(0.01));
    }

    @Test
    void lanzaErrorSiElAlmacenNoExiste() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act y Assert
        assertThatThrownBy(() -> useCase.calcular(id, new Ubicacion(0, 0)))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
