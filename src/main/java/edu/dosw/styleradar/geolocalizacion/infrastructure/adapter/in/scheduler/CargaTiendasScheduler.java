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

    public CargaTiendasScheduler(SincronizarTiendasUseCase sincronizar) {
        this.sincronizar = sincronizar;
    }

    @Scheduled(initialDelayString = "${tiendas.carga.retraso-inicial:PT20S}",
            fixedDelayString = "${tiendas.carga.intervalo:PT30M}")
    public void cargar() {
        try {
            SincronizarTiendasUseCase.CargaTiendas carga = sincronizar.cargarDesdeTiendas();
            log.info("Carga desde ms-tiendas: {} almacenes, {} fundaciones, {} descartados",
                    carga.almacenes(), carga.fundaciones(), carga.descartados());
        } catch (RuntimeException e) {
            log.warn("No se pudo cargar los almacenes y fundaciones de ms-tiendas: {}", e.getMessage());
        }
    }
}
