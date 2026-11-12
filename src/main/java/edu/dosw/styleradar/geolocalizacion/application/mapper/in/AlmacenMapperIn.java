package edu.dosw.styleradar.geolocalizacion.application.mapper.in;

import edu.dosw.styleradar.geolocalizacion.application.dto.response.AlmacenResponse;
import edu.dosw.styleradar.geolocalizacion.application.dto.response.DistanciaResponse;
import edu.dosw.styleradar.geolocalizacion.domain.model.Almacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.Distancia;

import java.util.UUID;

public final class AlmacenMapperIn {

    private AlmacenMapperIn() {
    }

    public static AlmacenResponse toResponse(Almacen almacen) {
        String categoria = almacen.categoria() == null ? null : almacen.categoria().nombre();
        return new AlmacenResponse(almacen.id(), almacen.nombre(), categoria,
                almacen.ubicacion().latitud(), almacen.ubicacion().longitud());
    }

    public static DistanciaResponse toResponse(UUID almacenId, Distancia distancia) {
        return new DistanciaResponse(almacenId, Math.round(distancia.kilometros() * 100) / 100.0);
    }
}
