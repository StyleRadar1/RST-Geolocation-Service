package edu.dosw.styleradar.geolocalizacion.domain.port.out;

import java.util.List;
import java.util.UUID;

public interface TiendasPort {

    List<AlmacenTienda> almacenes();

    List<FundacionTienda> fundaciones();

    record AlmacenTienda(UUID id, String nit, String nombre, String categoria, Double latitud, Double longitud) {
    }

    record FundacionTienda(UUID id, String nombre, Double latitud, Double longitud) {
    }
}
