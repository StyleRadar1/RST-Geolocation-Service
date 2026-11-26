package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.pagos;

import edu.dosw.styleradar.geolocalizacion.domain.port.out.PagosPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PagosAdapter implements PagosPort {

    private final PagosClient client;
    private final boolean filtroHabilitado;

    public PagosAdapter(PagosClient client, @Value("${pagos.filtro-habilitado:false}") boolean filtroHabilitado) {
        this.client = client;
        this.filtroHabilitado = filtroHabilitado;
    }

    @Override
    public Optional<Set<String>> nitsAlDia() {
        if (!filtroHabilitado) {
            return Optional.empty();
        }
        TiendasAlDiaResponse respuesta = client.tiendasAlDia();
        // Una respuesta sin lista no significa "nadie ha pagado": la carga falla y el mapa queda como estaba
        if (respuesta == null || respuesta.taxIds() == null) {
            throw new IllegalStateException("ms-pagos no entrego la lista de tiendas al dia");
        }
        return Optional.of(respuesta.taxIds().stream().collect(Collectors.toSet()));
    }
}
