package edu.dosw.styleradar.geolocalizacion.domain.port.out;

import java.util.Optional;
import java.util.Set;

public interface PagosPort {

    // NIT de las tiendas con suscripcion al dia. Vacio (Optional) si el filtro esta apagado
    Optional<Set<String>> nitsAlDia();
}
