package edu.dosw.styleradar.geolocalizacion.application.usecase;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import edu.dosw.styleradar.geolocalizacion.domain.exception.DomainException;
import edu.dosw.styleradar.geolocalizacion.domain.model.Almacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.CategoriaAlmacen;
import edu.dosw.styleradar.geolocalizacion.domain.model.Fundacion;
import edu.dosw.styleradar.geolocalizacion.domain.model.Ubicacion;
import edu.dosw.styleradar.geolocalizacion.domain.port.in.SincronizarTiendasUseCase;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.FundacionRepositoryPort;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.TiendasPort;

import java.util.UUID;

public class SincronizarTiendasUseCaseImpl implements SincronizarTiendasUseCase {

    private final AlmacenRepositoryPort almacenRepository;
    private final FundacionRepositoryPort fundacionRepository;
    private final TiendasPort tiendas;

    public SincronizarTiendasUseCaseImpl(AlmacenRepositoryPort almacenRepository, FundacionRepositoryPort fundacionRepository,
                                         TiendasPort tiendas) {
        this.almacenRepository = almacenRepository;
        this.fundacionRepository = fundacionRepository;
        this.tiendas = tiendas;
    }

    @Override
    public Almacen guardarAlmacen(UUID id, String nit, String nombre, String categoria, Ubicacion ubicacion) {
        validar(id, nombre, ubicacion);
        if (categoria == null || categoria.isBlank()) {
            throw new DomainException("El almacén debe tener una categoría", "EVENTO_INVALIDO");
        }
        String nombreCategoria = categoria.trim();
        CategoriaAlmacen categoriaAlmacen = almacenRepository.findCategoriaByNombre(nombreCategoria)
                .orElseGet(() -> new CategoriaAlmacen(UUID.randomUUID(), nombreCategoria));
        String nitLimpio = nit == null || nit.isBlank() ? null : nit.trim();
        return almacenRepository.save(new Almacen(id, nombre.trim(), categoriaAlmacen, ubicacion, nitLimpio));
    }

    @Override
    public Fundacion guardarFundacion(UUID id, String nombre, Ubicacion ubicacion) {
        validar(id, nombre, ubicacion);
        return fundacionRepository.save(new Fundacion(id, nombre.trim(), ubicacion));
    }

    @Override
    public CargaTiendas cargarDesdeTiendas() {
        int almacenes = 0;
        int fundaciones = 0;
        int descartados = 0;
        List<TiendasPort.AlmacenTienda> vigentes = tiendas.almacenes();
        for (TiendasPort.AlmacenTienda a : vigentes) {
            try {
                guardarAlmacen(a.id(), a.nit(), a.nombre(), a.categoria(), ubicacion(a.latitud(), a.longitud()));
                almacenes++;
            } catch (DomainException e) {
                descartados++;
            }
        }
        for (TiendasPort.FundacionTienda f : tiendas.fundaciones()) {
            try {
                guardarFundacion(f.id(), f.nombre(), ubicacion(f.latitud(), f.longitud()));
                fundaciones++;
            } catch (DomainException e) {
                descartados++;
            }
        }
        return new CargaTiendas(almacenes, fundaciones, descartados, retirarNoVigentes(vigentes));
    }

    // ms-tiendas solo lista almacenes aprobados: el que ya no aparece (eliminado o sin verificacion) sale del mapa.
    // Con una lista vacia no se borra nada, y los registros sin NIT (datos de ejemplo o antiguos) se conservan.
    private int retirarNoVigentes(List<TiendasPort.AlmacenTienda> vigentes) {
        Set<String> nits = vigentes.stream()
                .map(TiendasPort.AlmacenTienda::nit)
                .filter(Objects::nonNull)
                .map(String::trim)
                .collect(Collectors.toSet());
        if (nits.isEmpty()) {
            return 0;
        }
        List<Almacen> sobrantes = almacenRepository.findAll().stream()
                .filter(a -> a.nit() != null && !nits.contains(a.nit().trim()))
                .toList();
        sobrantes.forEach(a -> almacenRepository.deleteById(a.id()));
        return sobrantes.size();
    }

    @Override
    public void retirarFundacion(UUID id) {
        if (id == null) {
            throw new DomainException("El evento debe traer el id de la fundación", "EVENTO_INVALIDO");
        }
        fundacionRepository.deleteById(id);
    }

    private static Ubicacion ubicacion(Double latitud, Double longitud) {
        return latitud == null || longitud == null ? null : new Ubicacion(latitud, longitud);
    }

    private static void validar(UUID id, String nombre, Ubicacion ubicacion) {
        if (id == null || nombre == null || nombre.isBlank() || ubicacion == null) {
            throw new DomainException("El evento debe traer id, nombre y ubicación", "EVENTO_INVALIDO");
        }
    }
}
