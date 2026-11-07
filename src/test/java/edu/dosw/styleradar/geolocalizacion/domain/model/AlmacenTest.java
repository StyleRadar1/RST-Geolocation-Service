package edu.dosw.styleradar.geolocalizacion.domain.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AlmacenTest {

    private final CategoriaAlmacen urbano = new CategoriaAlmacen(UUID.randomUUID(), "Urbano");

    @Test
    void perteneceASuCategoria() {
        Almacen almacen = new Almacen(UUID.randomUUID(), "Calle 85", urbano, new Ubicacion(4.66, -74.05));

        assertThat(almacen.perteneceA(urbano.id())).isTrue();
        assertThat(almacen.perteneceA(UUID.randomUUID())).isFalse();
    }

    @Test
    void sinCategoriaNoPerteneceANinguna() {
        Almacen almacen = new Almacen(UUID.randomUUID(), "Sin categoría", null, new Ubicacion(4.66, -74.05));

        assertThat(almacen.perteneceA(urbano.id())).isFalse();
    }

    @Test
    void calculaLaDistanciaDesdeSuUbicacion() {
        Almacen almacen = new Almacen(UUID.randomUUID(), "Calle 85", urbano, new Ubicacion(0, 0));

        assertThat(almacen.distanciaA(new Ubicacion(0, 0)).kilometros()).isZero();
    }
}
