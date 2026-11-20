package edu.dosw.styleradar.geolocalizacion.domain.port.in;

import edu.dosw.styleradar.geolocalizacion.domain.model.Almacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.Fundacion;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;

import java.util.UUID;

public interface SincronizarTiendasUseCase {

    Almacen guardarAlmacen(UUID id, String nombre, String categoria, Ubicacion ubicacion);

    Fundacion guardarFundacion(UUID id, String nombre, Ubicacion ubicacion);

    CargaTiendas cargarDesdeTiendas();

    record CargaTiendas(int almacenes, int fundaciones, int descartados) {
    }
}
