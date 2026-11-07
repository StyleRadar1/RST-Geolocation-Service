package edu.dosw.styleradar.geolocalizacion.application.mapper.out;

import edu.dosw.styleradar.geolocalizacion.domain.model.Fundacion;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;
import edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.entity.FundacionEntity;

public final class FundacionMapperOut {

    private FundacionMapperOut() {
    }

    public static Fundacion toDomain(FundacionEntity entity) {
        Ubicacion ubicacion = new Ubicacion(entity.getLatitud().doubleValue(), entity.getLongitud().doubleValue());
        return new Fundacion(entity.getId(), entity.getNombre(), ubicacion);
    }
}
