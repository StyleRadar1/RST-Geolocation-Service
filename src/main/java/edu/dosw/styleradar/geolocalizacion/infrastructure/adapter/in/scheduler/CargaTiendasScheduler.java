package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.scheduler;

import edu.dosw.styleradar.geolocalizacion.domain.port.in.SincronizarTiendasUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "tiendas.carga.habilitada", havingValue = "true", matchIfMissing = true)
public class CargaTiendasScheduler {

    private static final Logger log = LoggerFactory.getLogger(CargaTiendasScheduler.class);

    private final SincronizarTiendasUseCase sincronizar;
    private volatile boolean ultimaCargaFallo;

    public CargaTiendasScheduler(SincronizarTiendasUseCase sincronizar) {
        this.sincronizar = sincronizar;
    }

    @Scheduled(initialDelayString = "${tiendas.carga.retraso-inicial:PT20S}",
            fixedDelayString = "${tiendas.carga.intervalo:PT30M}")
    public void cargar() {
        try {
            SincronizarTiendasUseCase.CargaTiendas carga = sincronizar.cargarDesdeTiendas();
            log.info("Carga desde ms-tiendas: {} almacenes, {} fundaciones, {} descartados, {} retirados",
                    carga.almacenes(), carga.fundaciones(), carga.descartados(), carga.retirados());
            ultimaCargaFallo = false;
        } catch (RuntimeException e) {
            ultimaCargaFallo = true;
            log.warn("No se pudo cargar los almacenes y fundaciones de ms-tiendas: {}", e.getMessage());
        }
    }

    // Si la ultima carga fallo (por ejemplo, un servicio arrancando en frio), no se espera al siguiente ciclo completo
    @Scheduled(initialDelayString = "${tiendas.carga.reintento:PT2M}",
            fixedDelayString = "${tiendas.carga.reintento:PT2M}")
    public void reintentar() {
        if (ultimaCargaFallo) {
            cargar();
        }
    }
}
