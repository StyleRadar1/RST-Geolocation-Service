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
    @GetMapping({"/foundations/nearby", "/fundaciones/cercanas"})
    public ResponseEntity<List<FundacionResponse>> cercanas(@RequestParam double lat,
                                                            @RequestParam double lng,
                                                            @RequestParam(defaultValue = "5") double radioKm) {
        Ubicacion usuario = new Ubicacion(lat, lng);
        return ResponseEntity.ok(consultarFundaciones.consultar(usuario, new Distancia(radioKm)).stream()
                .map(fundacion -> FundacionMapperIn.toResponse(fundacion, usuario))
                .toList());
    }
}
