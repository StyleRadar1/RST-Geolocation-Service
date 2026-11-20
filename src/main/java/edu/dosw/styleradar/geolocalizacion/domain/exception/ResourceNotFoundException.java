package edu.dosw.styleradar.geolocalizacion.domain.exception;

public class ResourceNotFoundException extends DomainException {

    public ResourceNotFoundException(String message, String codigo) {
        super(message, codigo);
    }
}
