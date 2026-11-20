package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.tiendas;

import java.util.Set;

public record AlmacenTiendasResponse(String nit, String nombreComercial, Double latitud, Double longitud,
                                     Set<String> categorias) {
}
