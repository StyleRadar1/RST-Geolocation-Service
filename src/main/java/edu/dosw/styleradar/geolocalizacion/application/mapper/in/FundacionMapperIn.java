package edu.dosw.styleradar.geolocalizacion.application.mapper.in;

import edu.dosw.styleradar.geolocalizacion.application.dto.response.FundacionResponse;
import edu.dosw.styleradar.geolocalizacion.domain.model.Fundacion;

public final class FundacionMapperIn {

    private FundacionMapperIn() {
    }

    public static FundacionResponse toResponse(Fundacion fundacion) {
        return new FundacionResponse(fundacion.id(), fundacion.nombre(),
                fundacion.ubicacion().latitud(), fundacion.ubicacion().longitud());
    }
}
