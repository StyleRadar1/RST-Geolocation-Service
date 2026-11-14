package edu.dosw.styleradar.geolocalizacion.domain.port.in;

import edu.dosw.styleradar.geolocalizacion.domain.model.Almacen;

import java.util.List;
import java.util.UUID;

public interface ConsultarAlmacenesUseCase {

    List<Almacen> consultar(UUID categoriaId);
}
