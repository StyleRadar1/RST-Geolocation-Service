package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.rest.doc;

import edu.dosw.styleradar.geolocalizacion.application.dto.response.AlmacenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Stores", description = "Allied stores on the map")
public interface AlmacenApi {

    @Operation(summary = "List map stores",
            description = "Returns the allied stores within the radius, nearest first, with their distanciaKm. "
                    + "If categoriaId is sent, only the stores of that category (SR-RF-17, SR-RF-18).")
    @ApiResponse(responseCode = "200", description = "Store list")
    @ApiResponse(responseCode = "400", description = "lat or lng is missing or out of range, radioKm is not valid or categoriaId is not a UUID")
    ResponseEntity<List<AlmacenResponse>> listar(
            @Parameter(description = "User latitude", example = "4.6762") double lat,
            @Parameter(description = "User longitude", example = "-74.0481") double lng,
            @Parameter(description = "Radius in km, greater than 0 and up to 20", example = "5") double radioKm,
            @Parameter(description = "Filters by store category") UUID categoriaId);
}
