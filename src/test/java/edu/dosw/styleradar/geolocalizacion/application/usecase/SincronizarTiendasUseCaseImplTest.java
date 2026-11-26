package edu.dosw.styleradar.geolocalizacion.application.usecase;

import edu.dosw.styleradar.geolocalizacion.domain.exception.DomainException;
import edu.dosw.styleradar.geolocalizacion.domain.model.Almacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.CategoriaAlmacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.Fundacion;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.SincronizarTiendasUseCase;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.FundacionRepositoryPort;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.TiendasPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class SincronizarTiendasUseCaseImplTest {

    private final AlmacenRepositoryPort almacenes = mock(AlmacenRepositoryPort.class);
    private final FundacionRepositoryPort fundaciones = mock(FundacionRepositoryPort.class);
    private final TiendasPort tiendas = mock(TiendasPort.class);
    private final SincronizarTiendasUseCaseImpl useCase = new SincronizarTiendasUseCaseImpl(almacenes, fundaciones, tiendas);
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

        Almacen almacen = useCase.guardarAlmacen(UUID.randomUUID(), " 900555777-1 ", " Calle 85 ", "Urbano", ubicacion);

        assertThat(almacen.nombre()).isEqualTo("Calle 85");
        assertThat(almacen.categoria()).isEqualTo(urbano);
        assertThat(almacen.nit()).isEqualTo("900555777-1");
    }

    @Test
    void creaLaCategoriaSiNoExiste() {
        when(almacenes.findCategoriaByNombre("Vintage")).thenReturn(Optional.empty());

        Almacen almacen = useCase.guardarAlmacen(UUID.randomUUID(), " ", "Nora", "Vintage", ubicacion);

        assertThat(almacen.categoria().nombre()).isEqualTo("Vintage");
        assertThat(almacen.categoria().id()).isNotNull();
        assertThat(almacen.nit()).isNull();
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

        assertThatThrownBy(() -> useCase.guardarAlmacen(id, null, "Nora", " ", ubicacion)).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> useCase.guardarAlmacen(null, null, "Nora", "Vintage", ubicacion)).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> useCase.guardarFundacion(id, "", ubicacion)).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> useCase.guardarFundacion(id, "Abrigo", null)).isInstanceOf(DomainException.class);
        verifyNoInteractions(fundaciones);
    }

    @Test
    void cargaLosAlmacenesYFundacionesDeTiendasYDescartaLosInvalidos() {
        when(almacenes.findCategoriaByNombre("Casual")).thenReturn(Optional.empty());
        when(tiendas.almacenes()).thenReturn(List.of(
                new TiendasPort.AlmacenTienda(UUID.randomUUID(), "900123456-1", "Atelier", "Casual", 4.65, -74.06),
                new TiendasPort.AlmacenTienda(UUID.randomUUID(), null, "Sin categoria", null, 4.65, -74.06),
                new TiendasPort.AlmacenTienda(UUID.randomUUID(), null, "Fuera de rango", "Casual", 95.0, -74.06)));
        when(tiendas.fundaciones()).thenReturn(List.of(
                new TiendasPort.FundacionTienda(UUID.randomUUID(), "Abrigo Solidario", 4.67, -74.05),
                new TiendasPort.FundacionTienda(UUID.randomUUID(), "Sin ubicacion", null, -74.05)));

        SincronizarTiendasUseCase.CargaTiendas carga = useCase.cargarDesdeTiendas();

        assertThat(carga).isEqualTo(new SincronizarTiendasUseCase.CargaTiendas(1, 1, 3, 0));
    }

    @Test
    void retiraLosAlmacenesQueTiendasYaNoLista() {
        CategoriaAlmacen casual = new CategoriaAlmacen(UUID.randomUUID(), "Casual");
        Almacen vigente = new Almacen(UUID.randomUUID(), "Vigente", casual, ubicacion, "900-1");
        Almacen retirado = new Almacen(UUID.randomUUID(), "Retirado", casual, ubicacion, "900-2");
        Almacen sinNit = new Almacen(UUID.randomUUID(), "Ejemplo", casual, ubicacion);
        when(almacenes.findCategoriaByNombre("Casual")).thenReturn(Optional.of(casual));
        when(almacenes.findAll()).thenReturn(List.of(vigente, retirado, sinNit));
        when(tiendas.almacenes()).thenReturn(List.of(
                new TiendasPort.AlmacenTienda(vigente.id(), "900-1", "Vigente", "Casual", 4.65, -74.06)));

        SincronizarTiendasUseCase.CargaTiendas carga = useCase.cargarDesdeTiendas();

        assertThat(carga.retirados()).isEqualTo(1);
        verify(almacenes).deleteById(retirado.id());
        verify(almacenes, never()).deleteById(vigente.id());
        verify(almacenes, never()).deleteById(sinNit.id());
    }

    @Test
    void conUnaListaVaciaNoRetiraNada() {
        when(tiendas.almacenes()).thenReturn(List.of());

        assertThat(useCase.cargarDesdeTiendas().retirados()).isZero();
        verify(almacenes, never()).deleteById(any());
    }

    @Test
    void retiraLaFundacion() {
        UUID id = UUID.randomUUID();

        useCase.retirarFundacion(id);

        verify(fundaciones).deleteById(id);
        assertThatThrownBy(() -> useCase.retirarFundacion(null)).isInstanceOf(DomainException.class);
    }
}
