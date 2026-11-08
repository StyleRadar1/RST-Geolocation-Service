package edu.dosw.styleradar.geolocalizacion.domain.model;

import edu.dosw.styleradar.geolocalizacion.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanciaTest {

    @Test
    void comparaDistancias() {
        Distancia corta = new Distancia(2);
        Distancia larga = new Distancia(5);

        assertThat(corta.esMenorQue(larga)).isTrue();
        assertThat(larga.esMenorQue(corta)).isFalse();
    }

    @Test
    void rechazaDistanciaNegativa() {
        assertThatThrownBy(() -> new Distancia(-1)).isInstanceOf(DomainException.class);
    }
}
