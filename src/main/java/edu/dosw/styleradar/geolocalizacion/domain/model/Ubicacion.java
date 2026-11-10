package edu.dosw.styleradar.geolocalizacion.domain.model;

import edu.dosw.styleradar.geolocalizacion.domain.exception.DomainException;

public record Ubicacion(double latitud, double longitud) {

    private static final double RADIO_TIERRA_KM = 6371.0;

    public Ubicacion {
        if (latitud < -90 || latitud > 90) {
            throw new DomainException("La latitud debe estar entre -90 y 90");
        }
        if (longitud < -180 || longitud > 180) {
            throw new DomainException("La longitud debe estar entre -180 y 180");
        }
    }

    // Fórmula de Haversine
    public Distancia distanciaA(Ubicacion otra) {
        double dLat = Math.toRadians(otra.latitud - latitud);
        double dLon = Math.toRadians(otra.longitud - longitud);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(latitud)) * Math.cos(Math.toRadians(otra.latitud))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return new Distancia(RADIO_TIERRA_KM * c);
    }
}
