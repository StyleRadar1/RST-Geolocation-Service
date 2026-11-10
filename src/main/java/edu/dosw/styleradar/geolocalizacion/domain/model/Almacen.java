package edu.dosw.styleradar.geolocalizacion.domain.model;

import java.util.UUID;

public record Almacen(UUID id, String nombre, CategoriaAlmacen categoria, Ubicacion ubicacion) {

    public boolean perteneceA(UUID categoriaId) {
        return categoria != null && categoria.id().equals(categoriaId);
    }

    public Distancia distanciaA(Ubicacion otra) {
        return ubicacion.distanciaA(otra);
    }
}
