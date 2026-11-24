package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.tiendas;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Set;

// ms-tiendas pasó sus campos a inglés en QA; se aceptan ambos nombres
public record AlmacenTiendasResponse(String nit,
                                     @JsonAlias("tradeName") String nombreComercial,
                                     @JsonAlias("latitude") Double latitud,
                                     @JsonAlias("longitude") Double longitud,
                                     @JsonAlias("categories") Set<String> categorias) {
}
