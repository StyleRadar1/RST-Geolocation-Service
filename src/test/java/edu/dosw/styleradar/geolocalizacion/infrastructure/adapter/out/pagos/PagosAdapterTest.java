package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.pagos;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class PagosAdapterTest {

    private final PagosClient client = mock(PagosClient.class);

    @Test
    void conElFiltroApagadoNoLlamaAPagos() {
        assertThat(new PagosAdapter(client, false).nitsAlDia()).isEmpty();
        verifyNoInteractions(client);
    }

    @Test
    void entregaLosNitAlDia() {
        when(client.tiendasAlDia()).thenReturn(new TiendasAlDiaResponse(List.of("900555777-1")));

        assertThat(new PagosAdapter(client, true).nitsAlDia()).contains(Set.of("900555777-1"));
    }

    @Test
    void fallaSiPagosNoEntregaLaLista() {
        when(client.tiendasAlDia()).thenReturn(null);

        assertThatThrownBy(() -> new PagosAdapter(client, true).nitsAlDia()).isInstanceOf(IllegalStateException.class);
    }
}
