package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.rest.doc;

import edu.dosw.styleradar.geolocalizacion.application.dto.response.DistanciaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Distancia", description = "Distancia entre el usuario y un almacén")
public interface DistanciaApi {

    @Operation(summary = "Calcular distancia a un almacén",
            description = "Calcula en kilómetros la distancia entre la ubicación del usuario y el almacén (SR-RF-21).")
    @ApiResponse(responseCode = "200", description = "Distancia calculada")
    @ApiResponse(responseCode = "400", description = "Faltan lat o lng, o no son números")
    @ApiResponse(responseCode = "404", description = "El almacén no existe")
    @ApiResponse(responseCode = "422", description = "Latitud o longitud fuera de rango")
    ResponseEntity<DistanciaResponse> calcular(
            @Parameter(description = "Id del almacén") UUID id,
            @Parameter(description = "Latitud del usuario", example = "4.7827") double lat,
            @Parameter(description = "Longitud del usuario", example = "-74.0427") double lng);
}
