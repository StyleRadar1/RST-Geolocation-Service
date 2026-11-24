package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.tiendas;

import edu.dosw.styleradar.geolocalizacion.domain.model.IdTiendas;
import edu.dosw.styleradar.geolocalizacion.domain.port.out.TiendasPort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Component
public class TiendasAdapter implements TiendasPort {

    static final int TAMANO_PAGINA = 100;
    static final int MAXIMO_PAGINAS = 50;
    private static final double LATITUD_BOGOTA = 4.711;
    private static final double LONGITUD_BOGOTA = -74.0721;

    private final TiendasClient client;

    public TiendasAdapter(TiendasClient client) {
        this.client = client;
    }

    @Override
    public List<AlmacenTienda> almacenes() {
        List<AlmacenTienda> almacenes = new ArrayList<>();
        int pagina = 0;
        PaginaTiendasResponse<AlmacenTiendasResponse> respuesta;
        do {
            respuesta = client.almacenes(pagina++, TAMANO_PAGINA);
            if (respuesta == null || respuesta.content() == null) {
                break;
            }
            respuesta.content().forEach(a -> almacenes.add(new AlmacenTienda(idAlmacen(a.nit()), a.nombreComercial(),
                    categoria(a.categorias()), a.latitud(), a.longitud())));
        } while (!respuesta.last() && pagina < MAXIMO_PAGINAS);
        return almacenes;
    }

    @Override
    public List<FundacionTienda> fundaciones() {
        List<FundacionTiendasResponse> respuesta = client.fundaciones(LATITUD_BOGOTA, LONGITUD_BOGOTA);
        if (respuesta == null) {
            return List.of();
        }
        return respuesta.stream()
                .filter(FundacionTiendasResponse::activa)
                .map(f -> new FundacionTienda(idFundacion(f.id()), f.nombre(), f.latitud(), f.longitud()))
                .toList();
    }

    static UUID idAlmacen(String nit) {
        return IdTiendas.almacen(nit);
    }

    static UUID idFundacion(Long id) {
        return IdTiendas.fundacion(id);
    }

    static String categoria(Set<String> categorias) {
        if (categorias == null || categorias.isEmpty()) {
            return null;
        }
        String valor = categorias.stream().sorted().findFirst().orElseThrow().trim().toUpperCase(Locale.ROOT);
        if (valor.equals("DEPORTIVA")) {
            return "Deportivo";
        }
        return valor.charAt(0) + valor.substring(1).toLowerCase(Locale.ROOT);
    }
}
