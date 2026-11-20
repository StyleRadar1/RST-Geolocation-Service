package edu.dosw.styleradar.geolocalizacion.application.usecase;

import edu.dosw.styleradar.geolocalizacion.domain.exception.ParametroInvalidoException;
import edu.dosw.styleradar.geolocalizacion.domain.model.Almacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.CategoriaAlmacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.AlmacenRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ConsultarAlmacenesUseCaseImplTest {

    private final AlmacenRepositoryPort repository = mock(AlmacenRepositoryPort.class);
    private final ConsultarAlmacenesUseCaseImpl useCase = new ConsultarAlmacenesUseCaseImpl(repository);
    private final CategoriaAlmacen formal = new CategoriaAlmacen(UUID.randomUUID(), "Formal");
    private final CategoriaAlmacen urbano = new CategoriaAlmacen(UUID.randomUUID(), "Urbano");
    private final Almacen lejano = new Almacen(UUID.randomUUID(), "Lejano", formal, new Ubicacion(0, 0.04));
    private final Almacen cercano = new Almacen(UUID.randomUUID(), "Cercano", urbano, new Ubicacion(0, 0.01));
    private final Almacen fueraDelRadio = new Almacen(UUID.randomUUID(), "Fuera", urbano, new Ubicacion(0, 1));

    @BeforeEach
    void setUp() {
        when(repository.findAll()).thenReturn(List.of(lejano, fueraDelRadio, cercano));
    }

    @Test
    void devuelveLosDelRadioDelMasCercanoAlMasLejano() {
        assertThat(useCase.consultar(null, 0, 0, 5)).containsExactly(cercano, lejano);
    }

    @Test
    void conCategoriaFiltraLosAlmacenes() {
        assertThat(useCase.consultar(urbano.id(), 0, 0, 5)).containsExactly(cercano);
    }

    @Test
    void rechazaUbicacionORadioInvalidos() {
        assertThatThrownBy(() -> useCase.consultar(null, 91, 0, 5)).isInstanceOf(ParametroInvalidoException.class);
        assertThatThrownBy(() -> useCase.consultar(null, 0, -181, 5)).isInstanceOf(ParametroInvalidoException.class);
        assertThatThrownBy(() -> useCase.consultar(null, 0, 0, 0)).isInstanceOf(ParametroInvalidoException.class);
        assertThatThrownBy(() -> useCase.consultar(null, 0, 0, 21)).isInstanceOf(ParametroInvalidoException.class);
    }

    @Test
    void noConsultaLaBaseSiLosParametrosSonInvalidos() {
        UseCaseSinBase sinBase = new UseCaseSinBase();
        assertThatThrownBy(() -> sinBase.useCase.consultar(null, 100, 0, 5)).isInstanceOf(ParametroInvalidoException.class);
        verifyNoInteractions(sinBase.repository);
    }

    private static final class UseCaseSinBase {
        private final AlmacenRepositoryPort repository = mock(AlmacenRepositoryPort.class);
        private final ConsultarAlmacenesUseCaseImpl useCase = new ConsultarAlmacenesUseCaseImpl(repository);
    }
}
