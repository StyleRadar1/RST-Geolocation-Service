package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence;

import edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.mapper.AlmacenMapperOut;
import edu.dosw.styleradar.geolocalizacion.domain.model.Almacen;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.repository.AlmacenJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class AlmacenRepositoryAdapter implements AlmacenRepositoryPort {

    private final AlmacenJpaRepository jpaRepository;

    public AlmacenRepositoryAdapter(AlmacenJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Almacen> findAll() {
        return jpaRepository.findAll().stream().map(AlmacenMapperOut::toDomain).toList();
    }

    @Override
    public Optional<Almacen> findById(UUID id) {
        return jpaRepository.findById(id).map(AlmacenMapperOut::toDomain);
    }
}
