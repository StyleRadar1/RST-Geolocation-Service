package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.mapper;

import edu.dosw.styleradar.geolocalizacion.domain.model.Almacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.CategoriaAlmacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;
import edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.entity.AlmacenEntity;
import edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.entity.CategoriaAlmacenEntity;

import java.math.BigDecimal;

public final class AlmacenMapperOut {

    private AlmacenMapperOut() {
    }

    public static Almacen toDomain(AlmacenEntity entity) {
        CategoriaAlmacen categoria = new CategoriaAlmacen(entity.getCategoria().getId(), entity.getCategoria().getNombre());
        Ubicacion ubicacion = new Ubicacion(entity.getLatitud().doubleValue(), entity.getLongitud().doubleValue());
        return new Almacen(entity.getId(), entity.getNombre(), categoria, ubicacion);
    }

    public static CategoriaAlmacenEntity toEntity(CategoriaAlmacen categoria) {
        return new CategoriaAlmacenEntity(categoria.id(), categoria.nombre());
    }

    public static AlmacenEntity toEntity(Almacen almacen) {
        return new AlmacenEntity(almacen.id(), toEntity(almacen.categoria()), almacen.nombre(),
                BigDecimal.valueOf(almacen.ubicacion().latitud()), BigDecimal.valueOf(almacen.ubicacion().longitud()));
    }
}
