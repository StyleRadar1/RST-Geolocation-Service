package edu.dosw.styleradar.geolocalizacion.infrastructure.config;

import edu.dosw.styleradar.geolocalizacion.application.usecase.CalcularDistanciaUseCaseImpl;
import edu.dosw.styleradar.geolocalizacion.application.usecase.ConsultarAlmacenesUseCaseImpl;
import edu.dosw.styleradar.geolocalizacion.application.usecase.ConsultarFundacionesCercanasUseCaseImpl;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.CalcularDistanciaUseCase;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.ConsultarAlmacenesUseCase;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.ConsultarFundacionesCercanasUseCase;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.FundacionRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Conecta los casos de uso con sus puertos sin anotar la capa de aplicación
@Configuration
public class BeanConfig {

    @Bean
    public ConsultarAlmacenesUseCase consultarAlmacenesUseCase(AlmacenRepositoryPort almacenRepository) {
        return new ConsultarAlmacenesUseCaseImpl(almacenRepository);
    }

    @Bean
    public CalcularDistanciaUseCase calcularDistanciaUseCase(AlmacenRepositoryPort almacenRepository) {
        return new CalcularDistanciaUseCaseImpl(almacenRepository);
    }

    @Bean
    public ConsultarFundacionesCercanasUseCase consultarFundacionesCercanasUseCase(FundacionRepositoryPort fundacionRepository) {
        return new ConsultarFundacionesCercanasUseCaseImpl(fundacionRepository);
    }
}
