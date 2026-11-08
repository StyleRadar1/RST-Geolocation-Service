package edu.dosw.styleradar.geolocalizacion.application.usecase;

import edu.dosw.styleradar.geolocalizacion.domain.model.Distancia;
import edu.dosw.styleradar.geolocalizacion.domain.model.Fundacion;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.FundacionRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConsultarFundacionesCercanasUseCaseImplTest {

    private final FundacionRepositoryPort repository = mock(FundacionRepositoryPort.class);
    private final ConsultarFundacionesCercanasUseCaseImpl useCase = new ConsultarFundacionesCercanasUseCaseImpl(repository);

    @Test
    void devuelveSoloLasFundacionesDentroDelRadio() {
        // Arrange
        Fundacion cerca = new Fundacion(UUID.randomUUID(), "Cerca", new Ubicacion(0, 0.01));
        Fundacion lejos = new Fundacion(UUID.randomUUID(), "Lejos", new Ubicacion(0, 1));
        when(repository.findAll()).thenReturn(List.of(cerca, lejos));

        // Act
        List<Fundacion> resultado = useCase.consultar(new Ubicacion(0, 0), new Distancia(5));

        // Assert
        assertThat(resultado).containsExactly(cerca);
    }
}
