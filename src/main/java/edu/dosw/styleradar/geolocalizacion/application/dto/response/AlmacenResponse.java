package edu.dosw.styleradar.geolocalizacion.application.dto.response;

import java.util.UUID;

public record AlmacenResponse(UUID id, String nombre, String categoria, double latitud, double longitud) {
}
