package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.amqp;

import edu.dosw.styleradar.geolocalizacion.domain.exception.DomainException;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.SincronizarTiendasUseCase;
import edu.dosw.styleradar.geolocalizacion.domain.model.IdTiendas;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TiendasEventListenerTest {

    private final SincronizarTiendasUseCase useCase = mock(SincronizarTiendasUseCase.class);
    private final TiendasEventListener listener = new TiendasEventListener(useCase);
    private final UUID id = UUID.randomUUID();

    @Test
    void guardaElAlmacenDelEvento() {
        listener.onAlmacen(Map.of("almacenId", id.toString(), "nombre", "Calle 85", "categoria", "Urbano",
                "latitud", 4.67, "longitud", "-74.05"));

        verify(useCase).guardarAlmacen(id, null, "Calle 85", "Urbano", new Ubicacion(4.67, -74.05));
    }

    @Test
    void leeElEventoDentroDeDataYLaCategoriaComoObjeto() {
        listener.onAlmacen(Map.of("data", Map.of("id", id.toString(), "nombre", "Nora",
                "categoria", Map.of("nombre", "Vintage"), "lat", 4.6, "lng", -74.1)));

        verify(useCase).guardarAlmacen(id, null, "Nora", "Vintage", new Ubicacion(4.6, -74.1));
    }

    @Test
    void sinUuidElIdDelAlmacenSaleDelNit() {
        listener.onAlmacen(Map.of("nit", "900555777-1", "tradeName", "Almacén Pruebas", "categoria", "Casual",
                "latitud", 4.6, "longitud", -74.1));

        verify(useCase).guardarAlmacen(IdTiendas.almacen("900555777-1"), "900555777-1", "Almacén Pruebas", "Casual",
                new Ubicacion(4.6, -74.1));
    }

    @Test
    void guardaLaFundacionDelEvento() {
        listener.onFundacion(Map.of("fundacionId", id.toString(), "nombre", "Abrigo Norte",
                "latitud", "4.7827", "longitud", -74.0427));

        verify(useCase).guardarFundacion(id, "Abrigo Norte", new Ubicacion(4.7827, -74.0427));
    }

    @Test
    void mandaALaColaDeErroresLosEventosInvalidos() {
        when(useCase.guardarAlmacen(eq(null), any(), any(), any(), any())).thenThrow(new DomainException("sin id"));

        assertThatThrownBy(() -> listener.onAlmacen(Map.of("almacenId", "no-es-uuid", "nombre", "X", "categoria", "Y",
                "latitud", 4.6, "longitud", -74.1))).isInstanceOf(AmqpRejectAndDontRequeueException.class);
        assertThatThrownBy(() -> listener.onAlmacen(Map.of("almacenId", id.toString(), "nombre", "X", "categoria", "Y")))
                .isInstanceOf(AmqpRejectAndDontRequeueException.class);
        assertThatThrownBy(() -> listener.onFundacion(Map.of("fundacionId", id.toString(), "nombre", "X",
                "latitud", "norte", "longitud", -74.1))).isInstanceOf(AmqpRejectAndDontRequeueException.class);
        assertThatThrownBy(() -> listener.onFundacion(Map.of("fundacionId", id.toString(), "nombre", "X",
                "latitud", 120, "longitud", -74.1))).isInstanceOf(AmqpRejectAndDontRequeueException.class);
    }

    @Test
    void retiraLaFundacionDesactivada() {
        listener.onEstadoFundacion(Map.of("fundacionId", 3, "nombreFundacion", "Abrigo"),
                "styleradar.tiendas.fundacion.desactivada");

        verify(useCase).retirarFundacion(IdTiendas.fundacion(3L));
    }

    @Test
    void recargaDesdeTiendasCuandoSeReactiva() {
        listener.onEstadoFundacion(Map.of("fundacionId", 3), "styleradar.tiendas.fundacion.reactivada");

        verify(useCase).cargarDesdeTiendas();
    }

    @Test
    void rechazaLaDesactivacionSinId() {
        doThrow(new DomainException("x", "EVENTO_INVALIDO")).when(useCase).retirarFundacion(null);

        assertThatThrownBy(() -> listener.onEstadoFundacion(Map.of("fundacionId", "abc"),
                "styleradar.tiendas.fundacion.desactivada")).isInstanceOf(AmqpRejectAndDontRequeueException.class);
    }

    @Test
    void leeElIdDeLaFundacionDentroDeDataYSinRoutingKeyLaRetira() {
        listener.onEstadoFundacion(Map.of("data", Map.of("id", "7")), null);

        verify(useCase).retirarFundacion(IdTiendas.fundacion(7L));
        assertThat(TiendasEventListener.leerLargo(Map.of(), "fundacionId")).isNull();
    }

    @Test
    void entiendeLasRoutingKeysEnInglesDeTiendas() {
        listener.onEstadoFundacion(Map.of("fundacionId", 5), "foundation.deactivated");
        listener.onEstadoFundacion(Map.of("fundacionId", 5), "foundation.reactivated");

        verify(useCase).retirarFundacion(IdTiendas.fundacion(5L));
        verify(useCase).cargarDesdeTiendas();
    }

    @Test
    void aceptaCamposEnInglesParaCoordenadasYNombre() {
        listener.onAlmacen(Map.of("almacenId", id.toString(), "name", "English Store", "categoria", "Casual",
                "latitude", 4.65, "longitude", -74.06));
        listener.onFundacion(Map.of("fundacionId", id.toString(), "name", "Warm Coat Foundation",
                "latitude", 4.71, "lon", -74.03));

        verify(useCase).guardarAlmacen(id, null, "English Store", "Casual", new Ubicacion(4.65, -74.06));
        verify(useCase).guardarFundacion(id, "Warm Coat Foundation", new Ubicacion(4.71, -74.03));
    }
}
