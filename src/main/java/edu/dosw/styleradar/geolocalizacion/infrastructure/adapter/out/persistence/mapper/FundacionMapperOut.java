package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.mapper;

import edu.dosw.styleradar.geolocalizacion.domain.model.Fundacion;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;
import edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.entity.FundacionEntity;

import java.math.BigDecimal;

public final class FundacionMapperOut {

    private FundacionMapperOut() {
    }

    public static Fundacion toDomain(FundacionEntity entity) {
        Ubicacion ubicacion = new Ubicacion(entity.getLatitud().doubleValue(), entity.getLongitud().doubleValue());
        return new Fundacion(entity.getId(), entity.getNombre(), ubicacion);
    }

    public static FundacionEntity toEntity(Fundacion fundacion) {
        return new FundacionEntity(fundacion.id(), fundacion.nombre(),
                BigDecimal.valueOf(fundacion.ubicacion().latitud()), BigDecimal.valueOf(fundacion.ubicacion().longitud()));
    }
}
