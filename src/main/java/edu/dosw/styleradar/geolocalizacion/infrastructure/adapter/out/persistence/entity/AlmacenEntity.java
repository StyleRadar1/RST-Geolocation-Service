package edu.dosw.styleradar.geolocalizacion.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "almacen", schema = "geolocalizacion")
public class AlmacenEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaAlmacenEntity categoria;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal latitud;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal longitud;

    protected AlmacenEntity() {
    }

    public AlmacenEntity(UUID id, CategoriaAlmacenEntity categoria, String nombre, BigDecimal latitud, BigDecimal longitud) {
        this.id = id;
        this.categoria = categoria;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public UUID getId() {
        return id;
    }

    public CategoriaAlmacenEntity getCategoria() {
        return categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }
}
