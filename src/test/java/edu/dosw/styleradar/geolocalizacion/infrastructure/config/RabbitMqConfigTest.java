package edu.dosw.styleradar.geolocalizacion.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class RabbitMqConfigTest {

    @Test
    void enlazaLaColaDeEstadoDeFundacionesConSusDosEventos() {
        RabbitMqConfig config = new RabbitMqConfig();
        ReflectionTestUtils.setField(config, "exchange", "styleradar.exchange");
        ReflectionTestUtils.setField(config, "dlx", "styleradar.dlx");
        ReflectionTestUtils.setField(config, "dlqRoutingKey", "styleradar.geolocalizacion.error");

        Queue cola = config.fundacionesEstadoQueue("styleradar.geolocalizacion.fundacion-estado.q");
        Binding desactivada = config.fundacionDesactivadaBinding(cola, "styleradar.tiendas.fundacion.desactivada");
        Binding reactivada = config.fundacionReactivadaBinding(cola, "styleradar.tiendas.fundacion.reactivada");

        assertThat(cola.getArguments()).containsEntry("x-dead-letter-exchange", "styleradar.dlx");
        assertThat(desactivada.getRoutingKey()).isEqualTo("styleradar.tiendas.fundacion.desactivada");
        assertThat(reactivada.getRoutingKey()).isEqualTo("styleradar.tiendas.fundacion.reactivada");
        assertThat(reactivada.getExchange()).isEqualTo("styleradar.exchange");
    }
}
