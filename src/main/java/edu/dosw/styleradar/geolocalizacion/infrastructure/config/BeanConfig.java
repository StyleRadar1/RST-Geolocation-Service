package edu.dosw.styleradar.geolocalizacion.infrastructure.config;

import edu.dosw.styleradar.geolocalizacion.application.usecase.CalcularDistanciaUseCaseImpl;
import edu.dosw.styleradar.geolocalizacion.application.usecase.ConsultarAlmacenesUseCaseImpl;
import edu.dosw.styleradar.geolocalizacion.application.usecase.ConsultarFundacionesCercanasUseCaseImpl;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.CalcularDistanciaUseCase;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.ConsultarAlmacenesUseCase;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.ConsultarFundacionesCercanasUseCase;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.FundacionRepositoryPort;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.TiendasPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import edu.dosw.styleradar.geolocalizacion.application.usecase.SincronizarTiendasUseCaseImpl;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.SincronizarTiendasUseCase;

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
    public SincronizarTiendasUseCase sincronizarTiendasUseCase(AlmacenRepositoryPort almacenRepository,
                                                               FundacionRepositoryPort fundacionRepository,
                                                               TiendasPort tiendas) {
        return new SincronizarTiendasUseCaseImpl(almacenRepository, fundacionRepository, tiendas);
    }

    @Bean
    public ConsultarFundacionesCercanasUseCase consultarFundacionesCercanasUseCase(FundacionRepositoryPort fundacionRepository) {
        return new ConsultarFundacionesCercanasUseCaseImpl(fundacionRepository);
    }
}
