package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.rest.doc;

import edu.dosw.styleradar.geolocalizacion.application.dto.response.DistanciaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Distance", description = "Distance between the user and a store")
public interface DistanciaApi {

    @Operation(summary = "Calculate distance to a store",
            description = "Calculates in kilometers the distance between the user location and the store (SR-RF-21).")
    @ApiResponse(responseCode = "200", description = "Distance calculated")
    @ApiResponse(responseCode = "400", description = "lat or lng is missing or not a number")
    @ApiResponse(responseCode = "404", description = "The store does not exist")
    @ApiResponse(responseCode = "422", description = "Latitude or longitude out of range")
    ResponseEntity<DistanciaResponse> calcular(
            @Parameter(description = "Store id") UUID id,
            @Parameter(description = "User latitude", example = "4.7827") Double lat,
            @Parameter(description = "User longitude", example = "-74.0427") Double lng,
            @Parameter(description = "Alias for lat", example = "4.7827") Double latitud,
            @Parameter(description = "Alias for lng", example = "-74.0427") Double longitud,
            @Parameter(description = "Alias for lng", example = "-74.0427") Double lon);
}
