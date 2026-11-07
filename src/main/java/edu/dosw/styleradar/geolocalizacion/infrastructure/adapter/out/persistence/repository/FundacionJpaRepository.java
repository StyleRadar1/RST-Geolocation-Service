package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.repository;

import edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.entity.FundacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FundacionJpaRepository extends JpaRepository<FundacionEntity, UUID> {
}
