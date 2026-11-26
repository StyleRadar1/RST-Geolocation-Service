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
    @GetMapping("/stores/{id}/distance")
    public ResponseEntity<DistanciaResponse> calcular(@PathVariable UUID id,
                                                      @RequestParam(required = false) Double lat,
                                                      @RequestParam(required = false) Double lng,
                                                      @RequestParam(required = false) Double latitud,
                                                      @RequestParam(required = false) Double longitud,
                                                      @RequestParam(required = false) Double lon) {
        Ubicacion usuario = CoordenadasRequest.resolverObligatoria(lat, latitud, lng, longitud, lon);
        return ResponseEntity.ok(AlmacenMapperIn.toResponse(id, calcularDistancia.calcular(id, usuario)));
    }
}
