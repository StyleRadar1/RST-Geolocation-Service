package edu.dosw.styleradar.geolocalizacion.domain.port.in;

import edu.dosw.styleradar.geolocalizacion.domain.model.Distancia;
import edu.dosw.styleradar.geolocalizacion.domain.model.Fundacion;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;

import java.util.List;

public interface ConsultarFundacionesCercanasUseCase {

    List<Fundacion> consultar(Ubicacion usuario, Distancia radio);
}
