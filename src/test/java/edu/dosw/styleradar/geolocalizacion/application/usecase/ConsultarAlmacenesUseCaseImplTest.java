package edu.dosw.styleradar.geolocalizacion.application.usecase;

import edu.dosw.styleradar.geolocalizacion.domain.model.Almacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.CategoriaAlmacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.AlmacenRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConsultarAlmacenesUseCaseImplTest {

    private final AlmacenRepositoryPort repository = mock(AlmacenRepositoryPort.class);
    private final ConsultarAlmacenesUseCaseImpl useCase = new ConsultarAlmacenesUseCaseImpl(repository);

    private final CategoriaAlmacen formal = new CategoriaAlmacen(UUID.randomUUID(), "Formal");
    private final CategoriaAlmacen urbano = new CategoriaAlmacen(UUID.randomUUID(), "Urbano");
    private final Almacen sastreria = new Almacen(UUID.randomUUID(), "Sastrería", formal, new Ubicacion(4.67, -74.04));
    private final Almacen calle = new Almacen(UUID.randomUUID(), "Calle 85", urbano, new Ubicacion(4.66, -74.05));

    @Test
    void sinCategoriaDevuelveTodos() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of(sastreria, calle));

        // Act
        List<Almacen> resultado = useCase.consultar(null);

        // Assert
        assertThat(resultado).containsExactly(sastreria, calle);
    }

    @Test
    void conCategoriaFiltraLosAlmacenes() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of(sastreria, calle));

        // Act
        List<Almacen> resultado = useCase.consultar(urbano.id());

        // Assert
        assertThat(resultado).containsExactly(calle);
    }
}
