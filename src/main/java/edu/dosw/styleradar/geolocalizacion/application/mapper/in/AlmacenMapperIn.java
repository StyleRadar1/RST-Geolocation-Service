package edu.dosw.styleradar.geolocalizacion.application.mapper.in;

import edu.dosw.styleradar.geolocalizacion.application.dto.response.AlmacenResponse;
import edu.dosw.styleradar.geolocalizacion.application.dto.response.DistanciaResponse;
import edu.dosw.styleradar.geolocalizacion.domain.model.Almacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.Distancia;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;

import java.util.UUID;

public final class AlmacenMapperIn {

    private AlmacenMapperIn() {
    }

    public static AlmacenResponse toResponse(Almacen almacen, Ubicacion usuario) {
        UUID categoriaId = almacen.categoria() == null ? null : almacen.categoria().id();
        String categoria = almacen.categoria() == null ? null : almacen.categoria().nombre();
        Double distanciaKm = usuario == null ? null : redondear(almacen.distanciaA(usuario));
        return new AlmacenResponse(almacen.id(), almacen.nombre(), categoriaId, categoria,
                almacen.ubicacion().latitud(), almacen.ubicacion().longitud(), distanciaKm);
    }

    public static DistanciaResponse toResponse(UUID almacenId, Distancia distancia) {
        return new DistanciaResponse(almacenId, redondear(distancia));
    }

    public static double redondear(Distancia distancia) {
        return Math.round(distancia.kilometros() * 100) / 100.0;
    }
}
