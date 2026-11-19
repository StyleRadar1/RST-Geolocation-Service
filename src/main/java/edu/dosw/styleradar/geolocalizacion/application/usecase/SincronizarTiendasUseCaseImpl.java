package edu.dosw.styleradar.geolocalizacion.application.usecase;

import edu.dosw.styleradar.geolocalizacion.domain.exception.DomainException;
import edu.dosw.styleradar.geolocalizacion.domain.model.Almacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.CategoriaAlmacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.Fundacion;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.SincronizarTiendasUseCase;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.FundacionRepositoryPort;

import java.util.UUID;

public class SincronizarTiendasUseCaseImpl implements SincronizarTiendasUseCase {

    private final AlmacenRepositoryPort almacenRepository;
    private final FundacionRepositoryPort fundacionRepository;

    public SincronizarTiendasUseCaseImpl(AlmacenRepositoryPort almacenRepository, FundacionRepositoryPort fundacionRepository) {
        this.almacenRepository = almacenRepository;
        this.fundacionRepository = fundacionRepository;
    }

    @Override
    public Almacen guardarAlmacen(UUID id, String nombre, String categoria, Ubicacion ubicacion) {
        validar(id, nombre, ubicacion);
        if (categoria == null || categoria.isBlank()) {
            throw new DomainException("El almacén debe tener una categoría", "EVENTO_INVALIDO");
        }
        String nombreCategoria = categoria.trim();
        CategoriaAlmacen categoriaAlmacen = almacenRepository.findCategoriaByNombre(nombreCategoria)
                .orElseGet(() -> new CategoriaAlmacen(UUID.randomUUID(), nombreCategoria));
        return almacenRepository.save(new Almacen(id, nombre.trim(), categoriaAlmacen, ubicacion));
    }

    @Override
    public Fundacion guardarFundacion(UUID id, String nombre, Ubicacion ubicacion) {
        validar(id, nombre, ubicacion);
        return fundacionRepository.save(new Fundacion(id, nombre.trim(), ubicacion));
    }

    private static void validar(UUID id, String nombre, Ubicacion ubicacion) {
        if (id == null || nombre == null || nombre.isBlank() || ubicacion == null) {
            throw new DomainException("El evento debe traer id, nombre y ubicación", "EVENTO_INVALIDO");
        }
    }
}
