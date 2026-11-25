package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.geolocalizacion.application.dto.response.FundacionResponse;
import edu.dosw.styleradar.geolocalizacion.application.mapper.in.FundacionMapperIn;
import edu.dosw.styleradar.geolocalizacion.domain.model.Distancia;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.ConsultarFundacionesCercanasUseCase;
import edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.rest.doc.FundacionApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FundacionController implements FundacionApi {

    private final ConsultarFundacionesCercanasUseCase consultarFundaciones;

    public FundacionController(ConsultarFundacionesCercanasUseCase consultarFundaciones) {
        this.consultarFundaciones = consultarFundaciones;
    }

    @Override
    @GetMapping("/foundations/nearby")
    public ResponseEntity<List<FundacionResponse>> cercanas(@RequestParam(required = false) Double lat,
                                                            @RequestParam(required = false) Double lng,
                                                            @RequestParam(required = false) Double latitud,
                                                            @RequestParam(required = false) Double longitud,
                                                            @RequestParam(required = false) Double lon,
                                                            @RequestParam(defaultValue = "5") double radioKm) {
        Ubicacion usuario = CoordenadasRequest.resolverObligatoria(lat, latitud, lng, longitud, lon);
        return ResponseEntity.ok(consultarFundaciones.consultar(usuario, new Distancia(radioKm)).stream()
                .map(fundacion -> FundacionMapperIn.toResponse(fundacion, usuario))
                .toList());
    }
}
