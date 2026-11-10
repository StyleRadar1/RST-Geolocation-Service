package edu.dosw.styleradar.geolocalizacion.application.mapper;

import edu.dosw.styleradar.geolocalizacion.application.dto.response.AlmacenResponse;
import edu.dosw.styleradar.geolocalizacion.application.dto.response.DistanciaResponse;
import edu.dosw.styleradar.geolocalizacion.application.dto.response.FundacionResponse;
import edu.dosw.styleradar.geolocalizacion.application.mapper.in.AlmacenMapperIn;
import edu.dosw.styleradar.geolocalizacion.application.mapper.in.FundacionMapperIn;
import edu.dosw.styleradar.geolocalizacion.domain.model.Almacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.CategoriaAlmacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.Distancia;
import edu.dosw.styleradar.geolocalizacion.domain.model.Fundacion;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MapperInTest {

    @Test
    void mapeaAlmacenConCategoria() {
        UUID id = UUID.randomUUID();
        Almacen almacen = new Almacen(id, "Calle 85", new CategoriaAlmacen(UUID.randomUUID(), "Urbano"), new Ubicacion(4.66, -74.05));

        AlmacenResponse response = AlmacenMapperIn.toResponse(almacen);

        assertThat(response).isEqualTo(new AlmacenResponse(id, "Calle 85", "Urbano", 4.66, -74.05));
    }

    @Test
    void mapeaAlmacenSinCategoria() {
        Almacen almacen = new Almacen(UUID.randomUUID(), "Sin categoría", null, new Ubicacion(4.66, -74.05));

        assertThat(AlmacenMapperIn.toResponse(almacen).categoria()).isNull();
    }

    @Test
    void redondeaLaDistanciaADosDecimales() {
        UUID id = UUID.randomUUID();

        DistanciaResponse response = AlmacenMapperIn.toResponse(id, new Distancia(2.34567));

        assertThat(response).isEqualTo(new DistanciaResponse(id, 2.35));
    }

    @Test
    void mapeaFundacion() {
        UUID id = UUID.randomUUID();

        FundacionResponse response = FundacionMapperIn.toResponse(new Fundacion(id, "Abrigo Norte", new Ubicacion(4.78, -74.04)));

        assertThat(response).isEqualTo(new FundacionResponse(id, "Abrigo Norte", 4.78, -74.04));
    }
}
