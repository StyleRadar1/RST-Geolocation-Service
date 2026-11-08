package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.rest.doc;

import edu.dosw.styleradar.geolocalizacion.application.dto.response.AlmacenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Almacenes", description = "Almacenes aliados en el mapa")
public interface AlmacenApi {

    @Operation(summary = "Listar almacenes del mapa",
            description = "Devuelve los almacenes aliados con su ubicación. Si se envía categoriaId, solo los de esa categoría (SR-RF-17, SR-RF-18).")
    @ApiResponse(responseCode = "200", description = "Lista de almacenes")
    @ApiResponse(responseCode = "400", description = "categoriaId no es un UUID válido")
    ResponseEntity<List<AlmacenResponse>> listar(
            @Parameter(description = "Filtra por categoría del almacén") UUID categoriaId);
}
