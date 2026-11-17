package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.repository;

import edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.entity.CategoriaAlmacenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoriaAlmacenJpaRepository extends JpaRepository<CategoriaAlmacenEntity, UUID> {

    Optional<CategoriaAlmacenEntity> findFirstByNombreIgnoreCase(String nombre);
}
