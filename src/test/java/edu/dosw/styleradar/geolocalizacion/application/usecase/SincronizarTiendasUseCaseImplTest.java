package edu.dosw.styleradar.geolocalizacion.application.usecase;

import edu.dosw.styleradar.geolocalizacion.domain.exception.DomainException;
import edu.dosw.styleradar.geolocalizacion.domain.model.Almacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.CategoriaAlmacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.Fundacion;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.FundacionRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class SincronizarTiendasUseCaseImplTest {

    private final AlmacenRepositoryPort almacenes = mock(AlmacenRepositoryPort.class);
    private final FundacionRepositoryPort fundaciones = mock(FundacionRepositoryPort.class);
    private final SincronizarTiendasUseCaseImpl useCase = new SincronizarTiendasUseCaseImpl(almacenes, fundaciones);
    private final Ubicacion ubicacion = new Ubicacion(4.67, -74.05);

    @BeforeEach
    void setUp() {
        when(almacenes.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(fundaciones.save(any())).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void guardaElAlmacenConUnaCategoriaExistente() {
        CategoriaAlmacen urbano = new CategoriaAlmacen(UUID.randomUUID(), "Urbano");
        when(almacenes.findCategoriaByNombre("Urbano")).thenReturn(Optional.of(urbano));

        Almacen almacen = useCase.guardarAlmacen(UUID.randomUUID(), " Calle 85 ", "Urbano", ubicacion);

        assertThat(almacen.nombre()).isEqualTo("Calle 85");
        assertThat(almacen.categoria()).isEqualTo(urbano);
    }

    @Test
    void creaLaCategoriaSiNoExiste() {
        when(almacenes.findCategoriaByNombre("Vintage")).thenReturn(Optional.empty());

        Almacen almacen = useCase.guardarAlmacen(UUID.randomUUID(), "Nora", "Vintage", ubicacion);

        assertThat(almacen.categoria().nombre()).isEqualTo("Vintage");
        assertThat(almacen.categoria().id()).isNotNull();
    }

    @Test
    void guardaLaFundacion() {
        UUID id = UUID.randomUUID();

        Fundacion fundacion = useCase.guardarFundacion(id, "Abrigo Norte", ubicacion);

        assertThat(fundacion.id()).isEqualTo(id);
        assertThat(fundacion.ubicacion()).isEqualTo(ubicacion);
    }

    @Test
    void rechazaEventosIncompletos() {
        UUID id = UUID.randomUUID();

        assertThatThrownBy(() -> useCase.guardarAlmacen(id, "Nora", " ", ubicacion)).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> useCase.guardarAlmacen(null, "Nora", "Vintage", ubicacion)).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> useCase.guardarFundacion(id, "", ubicacion)).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> useCase.guardarFundacion(id, "Abrigo", null)).isInstanceOf(DomainException.class);
        verifyNoInteractions(fundaciones);
    }
}
