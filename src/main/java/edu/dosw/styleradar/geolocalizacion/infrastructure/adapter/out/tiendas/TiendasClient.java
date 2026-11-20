package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.tiendas;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ms-tiendas", url = "${tiendas.url}")
public interface TiendasClient {

    @GetMapping("/stores")
    PaginaTiendasResponse<AlmacenTiendasResponse> almacenes(@RequestParam("page") int pagina,
                                                            @RequestParam("size") int tamano);

    @GetMapping("/foundations")
    List<FundacionTiendasResponse> fundaciones(@RequestParam("latitude") double latitud,
                                               @RequestParam("longitude") double longitud);
}
