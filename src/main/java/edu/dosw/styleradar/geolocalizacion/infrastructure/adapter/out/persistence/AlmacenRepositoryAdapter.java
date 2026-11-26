package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence;

import edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.mapper.AlmacenMapperOut;
import edu.dosw.styleradar.geolocalizacion.domain.model.Almacen;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.repository.AlmacenJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import edu.dosw.styleradar.geolocalizacion.domain.model.CategoriaAlmacen;
import edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.repository.CategoriaAlmacenJpaRepository;

@Component
public class AlmacenRepositoryAdapter implements AlmacenRepositoryPort {

    private final AlmacenJpaRepository jpaRepository;
    private final CategoriaAlmacenJpaRepository categoriaRepository;

    public AlmacenRepositoryAdapter(AlmacenJpaRepository jpaRepository, CategoriaAlmacenJpaRepository categoriaRepository) {
        this.jpaRepository = jpaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<Almacen> findAll() {
        return jpaRepository.findAll().stream().map(AlmacenMapperOut::toDomain).toList();
    }

    @Override
    public Optional<Almacen> findById(UUID id) {
        return jpaRepository.findById(id).map(AlmacenMapperOut::toDomain);
    }

    @Override
    public Optional<CategoriaAlmacen> findCategoriaByNombre(String nombre) {
        return categoriaRepository.findFirstByNombreIgnoreCase(nombre)
                .map(c -> new CategoriaAlmacen(c.getId(), c.getNombre()));
    }

    @Override
    public Almacen save(Almacen almacen) {
        categoriaRepository.save(AlmacenMapperOut.toEntity(almacen.categoria()));
        return AlmacenMapperOut.toDomain(jpaRepository.save(AlmacenMapperOut.toEntity(almacen)));
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
