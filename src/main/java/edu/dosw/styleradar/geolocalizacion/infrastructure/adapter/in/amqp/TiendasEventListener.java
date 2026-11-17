package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.amqp;

import edu.dosw.styleradar.geolocalizacion.domain.exception.DomainException;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.SincronizarTiendasUseCase;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class TiendasEventListener {

    private final SincronizarTiendasUseCase sincronizar;

    public TiendasEventListener(SincronizarTiendasUseCase sincronizar) {
        this.sincronizar = sincronizar;
    }

    @RabbitListener(queues = "${styleradar.rabbitmq.queues.almacenes}")
    public void onAlmacen(Map<String, Object> payload) {
        try {
            sincronizar.guardarAlmacen(leerUuid(payload, "almacenId", "tiendaId", "id"), leerTexto(payload, "nombre"),
                    leerCategoria(payload), leerUbicacion(payload));
        } catch (DomainException e) {
            throw new AmqpRejectAndDontRequeueException("Evento de almacén inválido: " + e.getMessage());
        }
    }

    @RabbitListener(queues = "${styleradar.rabbitmq.queues.fundaciones}")
    public void onFundacion(Map<String, Object> payload) {
        try {
            sincronizar.guardarFundacion(leerUuid(payload, "fundacionId", "id"), leerTexto(payload, "nombre"),
                    leerUbicacion(payload));
        } catch (DomainException e) {
            throw new AmqpRejectAndDontRequeueException("Evento de fundación inválido: " + e.getMessage());
        }
    }

    static UUID leerUuid(Map<String, Object> payload, String... claves) {
        Object valor = leer(payload, claves);
        try {
            return valor == null ? null : UUID.fromString(valor.toString());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    static String leerTexto(Map<String, Object> payload, String... claves) {
        Object valor = leer(payload, claves);
        return valor == null ? null : valor.toString();
    }

    static String leerCategoria(Map<String, Object> payload) {
        Object valor = leer(payload, "categoria", "categoriaNombre");
        if (valor instanceof Map<?, ?> categoria) {
            Object nombre = categoria.get("nombre");
            return nombre == null ? null : nombre.toString();
        }
        return valor == null ? null : valor.toString();
    }

    static Ubicacion leerUbicacion(Map<String, Object> payload) {
        Double latitud = leerNumero(payload, "latitud", "lat");
        Double longitud = leerNumero(payload, "longitud", "lng");
        if (latitud == null || longitud == null) {
            throw new DomainException("El evento no trae una latitud y longitud válidas");
        }
        return new Ubicacion(latitud, longitud);
    }

    private static Double leerNumero(Map<String, Object> payload, String... claves) {
        Object valor = leer(payload, claves);
        if (valor instanceof Number numero) {
            return numero.doubleValue();
        }
        try {
            return valor == null ? null : Double.valueOf(valor.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Object leer(Map<String, Object> payload, String... claves) {
        if (payload == null) {
            return null;
        }
        Map<?, ?> data = payload.get("data") instanceof Map<?, ?> m ? m : Map.of();
        for (String clave : claves) {
            Object valor = payload.containsKey(clave) ? payload.get(clave) : data.get(clave);
            if (valor != null) {
                return valor;
            }
        }
        return null;
    }
}
