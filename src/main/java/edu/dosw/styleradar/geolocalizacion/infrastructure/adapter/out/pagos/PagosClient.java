package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.pagos;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ms-pagos", url = "${pagos.url:http://localhost:8400/api/v1}")
public interface PagosClient {

    @GetMapping("/subscriptions/stores/up-to-date")
    TiendasAlDiaResponse tiendasAlDia();
}
