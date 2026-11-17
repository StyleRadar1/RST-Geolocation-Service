package edu.dosw.styleradar.geolocalizacion.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${styleradar.rabbitmq.exchange}")
    private String exchange;

    @Value("${styleradar.rabbitmq.dlx}")
    private String dlx;

    @Value("${styleradar.rabbitmq.dlq}")
    private String dlq;

    @Value("${styleradar.rabbitmq.dlq-routing-key}")
    private String dlqRoutingKey;

    @Bean
    public TopicExchange styleradarExchange() {
        return new TopicExchange(exchange, true, false);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(dlx, true, false);
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(dlq).build();
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(dlqRoutingKey);
    }

    @Bean
    public Queue almacenesQueue(@Value("${styleradar.rabbitmq.queues.almacenes}") String nombre) {
        return colaConErrores(nombre);
    }

    @Bean
    public Queue fundacionesQueue(@Value("${styleradar.rabbitmq.queues.fundaciones}") String nombre) {
        return colaConErrores(nombre);
    }

    @Bean
    public Binding almacenRegistradoBinding(Queue almacenesQueue,
                                            @Value("${styleradar.rabbitmq.routing-keys.almacen-registrado}") String routingKey) {
        return BindingBuilder.bind(almacenesQueue).to(styleradarExchange()).with(routingKey);
    }

    @Bean
    public Binding almacenActualizadoBinding(Queue almacenesQueue,
                                             @Value("${styleradar.rabbitmq.routing-keys.almacen-actualizado}") String routingKey) {
        return BindingBuilder.bind(almacenesQueue).to(styleradarExchange()).with(routingKey);
    }

    @Bean
    public Binding fundacionRegistradaBinding(Queue fundacionesQueue,
                                              @Value("${styleradar.rabbitmq.routing-keys.fundacion-registrada}") String routingKey) {
        return BindingBuilder.bind(fundacionesQueue).to(styleradarExchange()).with(routingKey);
    }

    @Bean
    public Binding fundacionActualizadaBinding(Queue fundacionesQueue,
                                               @Value("${styleradar.rabbitmq.routing-keys.fundacion-actualizada}") String routingKey) {
        return BindingBuilder.bind(fundacionesQueue).to(styleradarExchange()).with(routingKey);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    private Queue colaConErrores(String nombre) {
        return QueueBuilder.durable(nombre)
                .deadLetterExchange(dlx)
                .deadLetterRoutingKey(dlqRoutingKey)
                .build();
    }
}
