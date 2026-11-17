package edu.dosw.styleradar.geolocalizacion.domain.model;

import java.util.UUID;

public record Fundacion(UUID id, String nombre, Ubicacion ubicacion) {

    public boolean estaCercaDe(Ubicacion punto, Distancia radio) {
        return distanciaA(punto).esMenorQue(radio);
    }

    public Distancia distanciaA(Ubicacion punto) {
        return ubicacion.distanciaA(punto);
    }
}
