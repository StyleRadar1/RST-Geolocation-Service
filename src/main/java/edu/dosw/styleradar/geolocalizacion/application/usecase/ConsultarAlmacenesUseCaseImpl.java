package edu.dosw.styleradar.geolocalizacion.application.usecase;

import edu.dosw.styleradar.geolocalizacion.domain.model.Almacen;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.ConsultarAlmacenesUseCase;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.AlmacenRepositoryPort;

import java.util.List;
import java.util.UUID;

public class ConsultarAlmacenesUseCaseImpl implements ConsultarAlmacenesUseCase {

    private final AlmacenRepositoryPort almacenRepository;

    public ConsultarAlmacenesUseCaseImpl(AlmacenRepositoryPort almacenRepository) {
        this.almacenRepository = almacenRepository;
    }

    @Override
    public List<Almacen> consultar(UUID categoriaId) {
        List<Almacen> almacenes = almacenRepository.findAll();
        if (categoriaId == null) {
            return almacenes;
        }
        return almacenes.stream().filter(a -> a.perteneceA(categoriaId)).toList();
    }
}
