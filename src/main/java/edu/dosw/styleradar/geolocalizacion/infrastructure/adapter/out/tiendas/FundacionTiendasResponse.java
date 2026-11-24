package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.tiendas;

import com.fasterxml.jackson.annotation.JsonAlias;

// ms-tiendas pasó sus campos a inglés en QA; se aceptan ambos nombres
public record FundacionTiendasResponse(Long id,
                                       @JsonAlias("name") String nombre,
                                       @JsonAlias("latitude") Double latitud,
                                       @JsonAlias("longitude") Double longitud,
                                       @JsonAlias("active") boolean activa) {
}
