package edu.dosw.styleradar.geolocalizacion.domain.model;

import edu.dosw.styleradar.geolocalizacion.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

class UbicacionTest {

    @Test
    void calculaLaDistanciaEntreDosPuntos() {
        // Arrange
        Ubicacion origen = new Ubicacion(0, 0);
        Ubicacion destino = new Ubicacion(0, 1);

        // Act
        Distancia distancia = origen.distanciaA(destino);

        // Assert
        assertThat(distancia.kilometros()).isCloseTo(111.19, within(0.01));
    }

    @Test
    void laDistanciaAlMismoPuntoEsCero() {
        Ubicacion punto = new Ubicacion(4.7827, -74.0427);

        assertThat(punto.distanciaA(punto).kilometros()).isZero();
    }

    @Test
    void rechazaLatitudFueraDeRango() {
        assertThatThrownBy(() -> new Ubicacion(91, 0)).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> new Ubicacion(-91, 0)).isInstanceOf(DomainException.class);
    }

    @Test
    void rechazaLongitudFueraDeRango() {
        assertThatThrownBy(() -> new Ubicacion(0, 181)).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> new Ubicacion(0, -181)).isInstanceOf(DomainException.class);
    }
}
