package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.geolocalizacion.application.dto.response.AlmacenResponse;
import edu.dosw.styleradar.geolocalizacion.application.mapper.in.AlmacenMapperIn;
import edu.dosw.styleradar.geolocalizacion.domain.exception.ParametroInvalidoException;
import edu.dosw.styleradar.geolocalizacion.domain.model.Almacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.ConsultarAlmacenesUseCase;
import edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.rest.doc.AlmacenApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class AlmacenController implements AlmacenApi {

    private final ConsultarAlmacenesUseCase consultarAlmacenes;

    public AlmacenController(ConsultarAlmacenesUseCase consultarAlmacenes) {
        this.consultarAlmacenes = consultarAlmacenes;
    }

    @Override
    @GetMapping({"/stores/nearby", "/almacenes"})
    public ResponseEntity<List<AlmacenResponse>> listar(@RequestParam(required = false) Double lat,
                                                        @RequestParam(required = false) Double lng,
                                                        @RequestParam(defaultValue = "5") double radioKm,
                                                        @RequestParam(required = false) UUID categoriaId) {
        if (lat == null && lng == null) {
            return ResponseEntity.ok(consultarAlmacenes.todos(categoriaId).stream()
                    .map(almacen -> AlmacenMapperIn.toResponse(almacen, null))
                    .toList());
        }
        if (lat == null || lng == null) {
            throw new ParametroInvalidoException("Falta un parámetro o tiene un formato inválido");
        }
        List<Almacen> almacenes = consultarAlmacenes.consultar(categoriaId, lat, lng, radioKm);
        Ubicacion usuario = new Ubicacion(lat, lng);
        return ResponseEntity.ok(almacenes.stream()
                .map(almacen -> AlmacenMapperIn.toResponse(almacen, usuario))
                .toList());
    }
}
