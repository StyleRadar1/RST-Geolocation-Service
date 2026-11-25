package edu.dosw.styleradar.geolocalizacion.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record AlmacenResponse(UUID id,
                              @Schema(description = "Store NIT in ms-tiendas, used to open its profile (/stores/{nit})",
                                      example = "900555777-1", nullable = true) String nit,
                              String nombre, UUID categoriaId, String categoria, double latitud, double longitud,
                              Double distanciaKm) {
}
