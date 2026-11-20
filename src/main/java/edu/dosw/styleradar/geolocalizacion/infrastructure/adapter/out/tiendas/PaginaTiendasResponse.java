package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.tiendas;

import java.util.List;

public record PaginaTiendasResponse<T>(List<T> content, boolean last) {
}
