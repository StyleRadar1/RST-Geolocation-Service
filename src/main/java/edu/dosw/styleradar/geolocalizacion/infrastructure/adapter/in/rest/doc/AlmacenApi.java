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
                    + "If categoriaId is sent, only the stores of that category (SR-RF-17, SR-RF-18). "
                    + "Each store includes its nit to open the store profile in ms-tiendas.")
    @ApiResponse(responseCode = "200", description = "Store list")
    @ApiResponse(responseCode = "400", description = "Only one of lat and lng was sent, a value is out of range, radioKm is not valid or categoriaId is not a UUID")
    ResponseEntity<List<AlmacenResponse>> listar(
            @Parameter(description = "User latitude; without lat and lng every store is listed by name and without distanciaKm", example = "4.6762") Double lat,
            @Parameter(description = "User longitude", example = "-74.0481") Double lng,
            @Parameter(description = "Radius in km, greater than 0 and up to 20", example = "5") double radioKm,
            @Parameter(description = "Filters by store category") UUID categoriaId);
}
