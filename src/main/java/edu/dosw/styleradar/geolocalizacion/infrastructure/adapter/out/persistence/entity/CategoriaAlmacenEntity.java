package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "categoria_almacen", schema = "geolocalizacion")
public class CategoriaAlmacenEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 60)
    private String nombre;

    protected CategoriaAlmacenEntity() {
    }

    public CategoriaAlmacenEntity(UUID id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}
