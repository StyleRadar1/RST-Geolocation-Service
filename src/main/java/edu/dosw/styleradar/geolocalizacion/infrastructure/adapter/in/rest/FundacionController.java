package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.geolocalizacion.application.dto.response.FundacionResponse;
import edu.dosw.styleradar.geolocalizacion.application.mapper.in.FundacionMapperIn;
import edu.dosw.styleradar.geolocalizacion.domain.model.Distancia;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.ConsultarFundacionesCercanasUseCase;
import edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.rest.doc.FundacionApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fundaciones")
public class FundacionController implements FundacionApi {

    private final ConsultarFundacionesCercanasUseCase consultarFundaciones;

    public FundacionController(ConsultarFundacionesCercanasUseCase consultarFundaciones) {
        this.consultarFundaciones = consultarFundaciones;
    }

    @Override
    @GetMapping("/cercanas")
    public ResponseEntity<List<FundacionResponse>> cercanas(@RequestParam double lat,
                                                            @RequestParam double lng,
                                                            @RequestParam(defaultValue = "5") double radioKm) {
        return ResponseEntity.ok(consultarFundaciones.consultar(new Ubicacion(lat, lng), new Distancia(radioKm)).stream()
                .map(FundacionMapperIn::toResponse)
                .toList());
    }
}
