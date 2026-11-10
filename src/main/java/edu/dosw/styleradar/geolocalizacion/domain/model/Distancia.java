package edu.dosw.styleradar.geolocalizacion.domain.model;

import edu.dosw.styleradar.geolocalizacion.domain.exception.DomainException;

public record Distancia(double kilometros) {

    public Distancia {
        if (kilometros < 0) {
            throw new DomainException("La distancia no puede ser negativa");
        }
    }

    public boolean esMenorQue(Distancia otra) {
        return kilometros < otra.kilometros;
    }
}
