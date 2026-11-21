package edu.dosw.styleradar.geolocalizacion.domain.model;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public final class IdTiendas {

    private IdTiendas() {
    }

    // UUID fijo a partir del id de ms-tiendas; ms-item-catalogo usa la misma regla para los almacenes
    public static UUID almacen(String nit) {
        return nit == null || nit.isBlank() ? null : uuid("almacen:" + nit.trim());
    }

    public static UUID fundacion(Long id) {
        return id == null ? null : uuid("fundacion:" + id);
    }

    private static UUID uuid(String clave) {
        return UUID.nameUUIDFromBytes(("ms-tiendas:" + clave).getBytes(StandardCharsets.UTF_8));
    }
}
