package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.tiendas;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TiendasRespuestasJsonTest {

    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Test
    void leeElAlmacenConLosNombresEnInglesDeQa() throws Exception {
        AlmacenTiendasResponse almacen = mapper.readValue("""
                {"tradeName":"Almacén Pruebas QA","latitude":4.6533,"longitude":-74.0602,"rating":0.0,
                 "categories":["CASUAL","FORMAL"],"nit":"900555777-1"}""", AlmacenTiendasResponse.class);

        assertThat(almacen).isEqualTo(new AlmacenTiendasResponse("900555777-1", "Almacén Pruebas QA", 4.6533, -74.0602,
                Set.of("CASUAL", "FORMAL")));
    }

    @Test
    void leeLaFundacionConLosNombresEnInglesDeQa() throws Exception {
        FundacionTiendasResponse fundacion = mapper.readValue("""
                {"id":1,"name":"Fundación Pruebas QA","latitude":4.6712,"longitude":-74.0531,"active":true}""",
                FundacionTiendasResponse.class);

        assertThat(fundacion).isEqualTo(new FundacionTiendasResponse(1L, "Fundación Pruebas QA", 4.6712, -74.0531, true));
    }

    @Test
    void sigueLeyendoLosNombresEnEspanol() throws Exception {
        FundacionTiendasResponse fundacion = mapper.readValue("""
                {"id":2,"nombre":"Abrigo","latitud":4.6,"longitud":-74.1,"activa":true}""", FundacionTiendasResponse.class);

        assertThat(fundacion.nombre()).isEqualTo("Abrigo");
        assertThat(fundacion.activa()).isTrue();
    }
}
