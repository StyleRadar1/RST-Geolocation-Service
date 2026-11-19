package edu.dosw.styleradar.geolocalizacion.application.usecase;

import edu.dosw.styleradar.geolocalizacion.domain.exception.ParametroInvalidoException;
import edu.dosw.styleradar.geolocalizacion.domain.model.Almacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.ConsultarAlmacenesUseCase;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.AlmacenRepositoryPort;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class ConsultarAlmacenesUseCaseImpl implements ConsultarAlmacenesUseCase {

    private static final double RADIO_MAXIMO_KM = 20;

    private final AlmacenRepositoryPort almacenRepository;

    public ConsultarAlmacenesUseCaseImpl(AlmacenRepositoryPort almacenRepository) {
        this.almacenRepository = almacenRepository;
    }

    @Override
    public List<Almacen> consultar(UUID categoriaId, double latitud, double longitud, double radioKm) {
        if (latitud < -90 || latitud > 90 || longitud < -180 || longitud > 180 || radioKm <= 0 || radioKm > RADIO_MAXIMO_KM) {
            throw new ParametroInvalidoException("Falta un parámetro o tiene un formato inválido");
        }
        Ubicacion usuario = new Ubicacion(latitud, longitud);
        return almacenRepository.findAll().stream()
                .filter(a -> categoriaId == null || a.perteneceA(categoriaId))
                .filter(a -> a.distanciaA(usuario).kilometros() <= radioKm)
                .sorted(Comparator.comparingDouble(a -> a.distanciaA(usuario).kilometros()))
                .toList();
    }
}
