package edu.dosw.styleradar.geolocalizacion.application.dto.response;

import java.util.UUID;

public record FundacionResponse(UUID id, String nombre, double latitud, double longitud) {
}
