package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.tiendas;

import edu.dosw.styleradar.geolocalizacion.domain.port.out.TiendasPort;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TiendasAdapterTest {

    private final TiendasClient client = mock(TiendasClient.class);
    private final TiendasAdapter adapter = new TiendasAdapter(client);

    @Test
    void recorreTodasLasPaginasDeAlmacenes() {
        AlmacenTiendasResponse atelier = new AlmacenTiendasResponse("900123456-1", "Atelier", 4.65, -74.06, Set.of("VINTAGE"));
        AlmacenTiendasResponse deportes = new AlmacenTiendasResponse("800555111-2", "Deportes", 4.70, -74.05,
                new LinkedHashSet<>(List.of("FORMAL", "DEPORTIVA")));
        when(client.almacenes(0, TiendasAdapter.TAMANO_PAGINA)).thenReturn(new PaginaTiendasResponse<>(List.of(atelier), false));
        when(client.almacenes(1, TiendasAdapter.TAMANO_PAGINA)).thenReturn(new PaginaTiendasResponse<>(List.of(deportes), true));

        List<TiendasPort.AlmacenTienda> almacenes = adapter.almacenes();

        assertThat(almacenes).hasSize(2);
        assertThat(almacenes.get(0).id()).isEqualTo(TiendasAdapter.idAlmacen("900123456-1"));
        assertThat(almacenes.get(0).nit()).isEqualTo("900123456-1");
        assertThat(almacenes.get(0).categoria()).isEqualTo("Vintage");
        assertThat(almacenes.get(1).categoria()).isEqualTo("Deportivo");
        verify(client).almacenes(1, TiendasAdapter.TAMANO_PAGINA);
    }

    @Test
    void fallaSiLaListaQuedaIncompleta() {
        AlmacenTiendasResponse atelier = new AlmacenTiendasResponse("900123456-1", "Atelier", 4.65, -74.06, Set.of("VINTAGE"));
        when(client.almacenes(0, TiendasAdapter.TAMANO_PAGINA)).thenReturn(new PaginaTiendasResponse<>(List.of(atelier), false));
        when(client.almacenes(1, TiendasAdapter.TAMANO_PAGINA)).thenReturn(null);

        assertThatThrownBy(adapter::almacenes).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void terminaSiTiendasNoRespondeContenido() {
        when(client.almacenes(0, TiendasAdapter.TAMANO_PAGINA)).thenReturn(null);

        assertThat(adapter.almacenes()).isEmpty();
    }

    @Test
    void cargaSoloLasFundacionesActivas() {
        when(client.fundaciones(anyDouble(), anyDouble())).thenReturn(List.of(
                new FundacionTiendasResponse(3L, "Abrigo Solidario", 4.67, -74.05, true),
                new FundacionTiendasResponse(4L, "Inactiva", 4.60, -74.08, false)));

        List<TiendasPort.FundacionTienda> fundaciones = adapter.fundaciones();

        assertThat(fundaciones).singleElement()
                .satisfies(f -> assertThat(f.id()).isEqualTo(TiendasAdapter.idFundacion(3L)));
    }

    @Test
    void sinFundacionesDevuelveListaVacia() {
        when(client.fundaciones(anyDouble(), anyDouble())).thenReturn(null);

        assertThat(adapter.fundaciones()).isEmpty();
    }

    @Test
    void losIdentificadoresSonEstablesYNulosSinDatos() {
        assertThat(TiendasAdapter.idAlmacen("900123456-1")).isEqualTo(TiendasAdapter.idAlmacen(" 900123456-1 "));
        assertThat(TiendasAdapter.idAlmacen(" ")).isNull();
        assertThat(TiendasAdapter.idFundacion(null)).isNull();
        assertThat(TiendasAdapter.categoria(Set.of())).isNull();
        assertThat(TiendasAdapter.categoria(null)).isNull();
        assertThat(TiendasAdapter.categoria(Set.of("casual"))).isEqualTo("Casual");
    }
}
