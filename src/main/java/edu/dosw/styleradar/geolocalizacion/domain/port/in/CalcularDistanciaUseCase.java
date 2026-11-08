package edu.dosw.styleradar.geolocalizacion.domain.port.in;

import edu.dosw.styleradar.geolocalizacion.domain.model.Distancia;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;

import java.util.UUID;

public interface CalcularDistanciaUseCase {

    Distancia calcular(UUID almacenId, Ubicacion usuario);
}
