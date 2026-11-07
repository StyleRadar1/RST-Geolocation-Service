package edu.dosw.styleradar.geolocalizacion.domain.port.out;

import edu.dosw.styleradar.geolocalizacion.domain.model.Fundacion;

import java.util.List;

public interface FundacionRepositoryPort {

    List<Fundacion> findAll();
}
