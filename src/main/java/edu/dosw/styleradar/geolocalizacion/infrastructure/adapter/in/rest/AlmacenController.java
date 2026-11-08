package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.geolocalizacion.application.dto.response.AlmacenResponse;
import edu.dosw.styleradar.geolocalizacion.application.mapper.in.AlmacenMapperIn;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.ConsultarAlmacenesUseCase;
import edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.rest.doc.AlmacenApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/almacenes")
public class AlmacenController implements AlmacenApi {

    private final ConsultarAlmacenesUseCase consultarAlmacenes;

    public AlmacenController(ConsultarAlmacenesUseCase consultarAlmacenes) {
        this.consultarAlmacenes = consultarAlmacenes;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<AlmacenResponse>> listar(@RequestParam(required = false) UUID categoriaId) {
        return ResponseEntity.ok(consultarAlmacenes.consultar(categoriaId).stream()
                .map(AlmacenMapperIn::toResponse)
                .toList());
    }
}
