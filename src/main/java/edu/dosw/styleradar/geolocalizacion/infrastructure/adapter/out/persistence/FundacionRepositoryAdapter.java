package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence;

import edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.mapper.FundacionMapperOut;
import edu.dosw.styleradar.geolocalizacion.domain.model.Fundacion;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.FundacionRepositoryPort;
import edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.repository.FundacionJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FundacionRepositoryAdapter implements FundacionRepositoryPort {

    private final FundacionJpaRepository jpaRepository;

    public FundacionRepositoryAdapter(FundacionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Fundacion> findAll() {
        return jpaRepository.findAll().stream().map(FundacionMapperOut::toDomain).toList();
    }
}
