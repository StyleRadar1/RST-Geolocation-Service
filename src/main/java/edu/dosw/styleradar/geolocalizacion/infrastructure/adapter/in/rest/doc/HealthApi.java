package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.rest.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Tag(name = "Estado", description = "Verificación del estado de ms-geolocalizacion")
public interface HealthApi {

    @Operation(summary = "Consultar el estado del servicio",
            description = "Indica si ms-geolocalizacion está en ejecución y responde en el puerto 8086.")
    @ApiResponse(responseCode = "200", description = "Servicio en ejecución",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"status\":\"UP\",\"service\":\"ms-geolocalizacion\",\"port\":8086}")))
    @ApiResponse(responseCode = "500", description = "Error interno del servicio",
            content = @Content(mediaType = "application/problem+json"))
    ResponseEntity<Map<String, Object>> health();
}
