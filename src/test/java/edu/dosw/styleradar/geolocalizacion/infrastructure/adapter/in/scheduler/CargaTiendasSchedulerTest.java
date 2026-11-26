package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.in.scheduler;

import edu.dosw.styleradar.geolocalizacion.domain.port.in.SincronizarTiendasUseCase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CargaTiendasSchedulerTest {

    private final SincronizarTiendasUseCase useCase = mock(SincronizarTiendasUseCase.class);
    private final CargaTiendasScheduler scheduler = new CargaTiendasScheduler(useCase);

    @Test
    void ejecutaLaCarga() {
        when(useCase.cargarDesdeTiendas()).thenReturn(new SincronizarTiendasUseCase.CargaTiendas(2, 1, 0, 0));

        scheduler.cargar();

        verify(useCase).cargarDesdeTiendas();
    }

    @Test
    void noFallaSiTiendasNoEstaDisponible() {
        when(useCase.cargarDesdeTiendas()).thenThrow(new IllegalStateException("ms-tiendas no responde"));

        assertThatCode(scheduler::cargar).doesNotThrowAnyException();
    }
}
