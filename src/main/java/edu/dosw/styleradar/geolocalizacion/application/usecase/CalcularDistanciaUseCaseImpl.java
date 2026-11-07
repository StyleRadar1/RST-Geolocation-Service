package edu.dosw.styleradar.geolocalizacion.application.usecase;

import edu.dosw.styleradar.geolocalizacion.domain.exception.ResourceNotFoundException;
import edu.dosw.styleradar.geolocalizacion.domain.model.Distancia;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.CalcularDistanciaUseCase;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.AlmacenRepositoryPort;

import java.util.UUID;

public class CalcularDistanciaUseCaseImpl implements CalcularDistanciaUseCase {

    private final AlmacenRepositoryPort almacenRepository;

    public CalcularDistanciaUseCaseImpl(AlmacenRepositoryPort almacenRepository) {
        this.almacenRepository = almacenRepository;
    }

    @Override
    public Distancia calcular(UUID almacenId, Ubicacion usuario) {
        return almacenRepository.findById(almacenId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el almacén " + almacenId))
                .distanciaA(usuario);
    }
}
