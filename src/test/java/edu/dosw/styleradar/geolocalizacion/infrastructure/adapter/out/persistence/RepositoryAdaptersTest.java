package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence;

import edu.dosw.styleradar.geolocalizacion.domain.model.Almacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.Fundacion;
import edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.entity.AlmacenEntity;
import edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.entity.CategoriaAlmacenEntity;
import edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.entity.FundacionEntity;
import edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.repository.AlmacenJpaRepository;
import edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.repository.FundacionJpaRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RepositoryAdaptersTest {

    private final CategoriaAlmacenEntity urbano = new CategoriaAlmacenEntity(UUID.randomUUID(), "Urbano");
    private final AlmacenEntity calle = new AlmacenEntity(UUID.randomUUID(), urbano, "Calle 85",
            new BigDecimal("4.668300"), new BigDecimal("-74.054600"));

    @Test
    void convierteLosAlmacenesAlDominio() {
        // Arrange
        AlmacenJpaRepository jpa = mock(AlmacenJpaRepository.class);
        when(jpa.findAll()).thenReturn(List.of(calle));

        // Act
        List<Almacen> almacenes = new AlmacenRepositoryAdapter(jpa).findAll();

        // Assert
        assertThat(almacenes).hasSize(1);
        assertThat(almacenes.get(0).categoria().nombre()).isEqualTo("Urbano");
        assertThat(almacenes.get(0).ubicacion().latitud()).isEqualTo(4.6683);
    }

    @Test
    void buscaUnAlmacenPorId() {
        // Arrange
        AlmacenJpaRepository jpa = mock(AlmacenJpaRepository.class);
        when(jpa.findById(calle.getId())).thenReturn(Optional.of(calle));

        // Act
        Optional<Almacen> almacen = new AlmacenRepositoryAdapter(jpa).findById(calle.getId());

        // Assert
        assertThat(almacen).isPresent();
        assertThat(almacen.get().nombre()).isEqualTo("Calle 85");
    }

    @Test
    void convierteLasFundacionesAlDominio() {
        // Arrange
        FundacionJpaRepository jpa = mock(FundacionJpaRepository.class);
        FundacionEntity entity = new FundacionEntity(UUID.randomUUID(), "Abrigo Norte",
                new BigDecimal("4.782700"), new BigDecimal("-74.042700"));
        when(jpa.findAll()).thenReturn(List.of(entity));

        // Act
        List<Fundacion> fundaciones = new FundacionRepositoryAdapter(jpa).findAll();

        // Assert
        assertThat(fundaciones).extracting(Fundacion::nombre).containsExactly("Abrigo Norte");
        assertThat(fundaciones.get(0).ubicacion().longitud()).isEqualTo(-74.0427);
    }
}
