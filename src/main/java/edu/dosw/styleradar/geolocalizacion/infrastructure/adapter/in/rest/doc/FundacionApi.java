package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.rest.doc;

import edu.dosw.styleradar.geolocalizacion.application.dto.response.FundacionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Fundaciones", description = "Fundaciones aliadas para donaciones")
public interface FundacionApi {

    @Operation(summary = "Consultar fundaciones cercanas",
            description = "Devuelve las fundaciones que están a menos de radioKm del usuario (SR-RF-43).")
    @ApiResponse(responseCode = "200", description = "Fundaciones cercanas")
    @ApiResponse(responseCode = "400", description = "Faltan lat o lng, o no son números")
    @ApiResponse(responseCode = "422", description = "Ubicación o radio inválidos")
    ResponseEntity<List<FundacionResponse>> cercanas(
            @Parameter(description = "Latitud del usuario", example = "4.7827") double lat,
            @Parameter(description = "Longitud del usuario", example = "-74.0427") double lng,
            @Parameter(description = "Radio de búsqueda en km", example = "5") double radioKm);
}
