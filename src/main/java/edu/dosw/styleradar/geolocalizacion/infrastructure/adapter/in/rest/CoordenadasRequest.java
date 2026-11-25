package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.geolocalizacion.domain.exception.ParametroInvalidoException;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;

final class CoordenadasRequest {

    private CoordenadasRequest() {
    }

    static Ubicacion resolverOpcional(Double lat, Double latitud, Double lng, Double longitud, Double lon) {
        Double resolvedLat = lat != null ? lat : latitud;
        Double resolvedLng = lng != null ? lng : (longitud != null ? longitud : lon);
        if (resolvedLat == null && resolvedLng == null) {
            return null;
        }
        if (resolvedLat == null || resolvedLng == null || resolvedLat < -90 || resolvedLat > 90 || resolvedLng < -180 || resolvedLng > 180) {
            throw new ParametroInvalidoException("Falta un parámetro o tiene un formato inválido");
        }
        return new Ubicacion(resolvedLat, resolvedLng);
    }

    static Ubicacion resolverObligatoria(Double lat, Double latitud, Double lng, Double longitud, Double lon) {
        Ubicacion ubicacion = resolverOpcional(lat, latitud, lng, longitud, lon);
        if (ubicacion == null) {
            throw new ParametroInvalidoException("Falta un parámetro o tiene un formato inválido");
        }
        return ubicacion;
    }
}
