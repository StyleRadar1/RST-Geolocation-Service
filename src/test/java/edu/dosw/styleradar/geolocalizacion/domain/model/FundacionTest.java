package edu.dosw.styleradar.geolocalizacion.domain.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FundacionTest {

    @Test
    void estaCercaSiEstaDentroDelRadio() {
        // Arrange
        Fundacion fundacion = new Fundacion(UUID.randomUUID(), "Ropa Solidaria", new Ubicacion(0, 1));
        Ubicacion usuario = new Ubicacion(0, 0);

        // Act y Assert
        assertThat(fundacion.estaCercaDe(usuario, new Distancia(200))).isTrue();
        assertThat(fundacion.estaCercaDe(usuario, new Distancia(50))).isFalse();
    }
}
