package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.mapper;

import edu.dosw.styleradar.geolocalizacion.domain.model.Almacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.CategoriaAlmacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;
import edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.entity.AlmacenEntity;

public final class AlmacenMapperOut {

    private AlmacenMapperOut() {
    }

    public static Almacen toDomain(AlmacenEntity entity) {
        CategoriaAlmacen categoria = new CategoriaAlmacen(entity.getCategoria().getId(), entity.getCategoria().getNombre());
        Ubicacion ubicacion = new Ubicacion(entity.getLatitud().doubleValue(), entity.getLongitud().doubleValue());
        return new Almacen(entity.getId(), entity.getNombre(), categoria, ubicacion);
    }
}
