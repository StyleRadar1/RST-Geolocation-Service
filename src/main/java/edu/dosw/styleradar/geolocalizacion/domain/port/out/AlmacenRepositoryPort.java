package edu.dosw.styleradar.geolocalizacion.domain.port.out;

import edu.dosw.styleradar.geolocalizacion.domain.model.Almacen;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import edu.dosw.styleradar.geolocalizacion.domain.model.CategoriaAlmacen;

public interface AlmacenRepositoryPort {

    List<Almacen> findAll();

    Optional<Almacen> findById(UUID id);

    Optional<CategoriaAlmacen> findCategoriaByNombre(String nombre);

    Almacen save(Almacen almacen);
}
