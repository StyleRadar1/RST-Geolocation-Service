package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.rest.doc;

import edu.dosw.styleradar.geolocalizacion.application.dto.response.FundacionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Foundations", description = "Allied foundations for donations")
public interface FundacionApi {

    @Operation(summary = "Get nearby foundations",
            description = "Returns the foundations within radioKm of the user, nearest first, with their distanciaKm (SR-RF-43).")
    @ApiResponse(responseCode = "200", description = "Nearby foundations")
    @ApiResponse(responseCode = "400", description = "lat or lng is missing or not a number")
    @ApiResponse(responseCode = "422", description = "Invalid location or radius")
    ResponseEntity<List<FundacionResponse>> cercanas(
            @Parameter(description = "User latitude", example = "4.7827") double lat,
            @Parameter(description = "User longitude", example = "-74.0427") double lng,
            @Parameter(description = "Search radius in km", example = "5") double radioKm);
}
