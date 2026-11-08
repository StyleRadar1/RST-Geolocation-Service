package edu.dosw.styleradar.geolocalizacion.application.usecase;

import edu.dosw.styleradar.geolocalizacion.domain.model.Distancia;
import edu.dosw.styleradar.geolocalizacion.domain.model.Fundacion;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.ConsultarFundacionesCercanasUseCase;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.FundacionRepositoryPort;

import java.util.List;

public class ConsultarFundacionesCercanasUseCaseImpl implements ConsultarFundacionesCercanasUseCase {

    private final FundacionRepositoryPort fundacionRepository;

    public ConsultarFundacionesCercanasUseCaseImpl(FundacionRepositoryPort fundacionRepository) {
        this.fundacionRepository = fundacionRepository;
    }

    @Override
    public List<Fundacion> consultar(Ubicacion usuario, Distancia radio) {
        return fundacionRepository.findAll().stream()
                .filter(f -> f.estaCercaDe(usuario, radio))
                .toList();
    }
}
