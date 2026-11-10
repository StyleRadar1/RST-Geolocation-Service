package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.geolocalizacion.application.dto.response.DistanciaResponse;
import edu.dosw.styleradar.geolocalizacion.application.mapper.in.AlmacenMapperIn;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.CalcularDistanciaUseCase;
import edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.rest.doc.DistanciaApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class DistanciaController implements DistanciaApi {

    private final CalcularDistanciaUseCase calcularDistancia;

    public DistanciaController(CalcularDistanciaUseCase calcularDistancia) {
        this.calcularDistancia = calcularDistancia;
    }

    @Override
    @GetMapping("/almacenes/{id}/distancia")
    public ResponseEntity<DistanciaResponse> calcular(@PathVariable UUID id,
                                                      @RequestParam double lat,
                                                      @RequestParam double lng) {
        return ResponseEntity.ok(AlmacenMapperIn.toResponse(id, calcularDistancia.calcular(id, new Ubicacion(lat, lng))));
    }
}
