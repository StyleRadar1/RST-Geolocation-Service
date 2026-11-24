package edu.dosw.styleradar.geolocalizacion.domain.port.out;

import edu.dosw.styleradar.geolocalizacion.domain.model.Fundacion;

import java.util.List;
import java.util.UUID;

public interface FundacionRepositoryPort {

    List<Fundacion> findAll();

    Fundacion save(Fundacion fundacion);

    void deleteById(UUID id);
}
