package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.geolocalizacion.application.dto.response.AlmacenResponse;
import edu.dosw.styleradar.geolocalizacion.application.mapper.in.AlmacenMapperIn;
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
    @GetMapping("/stores/nearby")
    public ResponseEntity<List<AlmacenResponse>> listar(@RequestParam(required = false) Double lat,
                                                        @RequestParam(required = false) Double lng,
                                                        @RequestParam(required = false) Double latitud,
                                                        @RequestParam(required = false) Double longitud,
                                                        @RequestParam(required = false) Double lon,
                                                        @RequestParam(defaultValue = "5") double radioKm,
                                                        @RequestParam(required = false) UUID categoriaId) {
        Ubicacion usuario = CoordenadasRequest.resolverOpcional(lat, latitud, lng, longitud, lon);
        if (usuario == null) {
            return ResponseEntity.ok(consultarAlmacenes.todos(categoriaId).stream()
                    .map(almacen -> AlmacenMapperIn.toResponse(almacen, null))
                    .toList());
        }
        List<Almacen> almacenes = consultarAlmacenes.consultar(categoriaId, usuario.latitud(), usuario.longitud(), radioKm);
        return ResponseEntity.ok(almacenes.stream()
                .map(almacen -> AlmacenMapperIn.toResponse(almacen, usuario))
                .toList());
    }
}
