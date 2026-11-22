package edu.dosw.styleradar.geolocalizacion.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdTiendasTest {

    @Test
    void calculaIdsEstablesYNulosSinDatos() {
        assertThat(IdTiendas.almacen(" 900123456-1 ")).isEqualTo(IdTiendas.almacen("900123456-1"));
        assertThat(IdTiendas.almacen(null)).isNull();
        assertThat(IdTiendas.almacen(" ")).isNull();
        assertThat(IdTiendas.fundacion(3L)).isNotNull().isNotEqualTo(IdTiendas.almacen("3"));
        assertThat(IdTiendas.fundacion(null)).isNull();
    }
}
