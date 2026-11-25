package edu.dosw.styleradar.geolocalizacion.domain.model;

import java.util.UUID;

// nit: id del almacén en ms-tiendas (/stores/{nit}); puede ser null en registros viejos
public record Almacen(UUID id, String nombre, CategoriaAlmacen categoria, Ubicacion ubicacion, String nit) {

    public Almacen(UUID id, String nombre, CategoriaAlmacen categoria, Ubicacion ubicacion) {
        this(id, nombre, categoria, ubicacion, null);
    }

    public boolean perteneceA(UUID categoriaId) {
        return categoria != null && categoria.id().equals(categoriaId);
    }

    public Distancia distanciaA(Ubicacion otra) {
        return ubicacion.distanciaA(otra);
    }
}
