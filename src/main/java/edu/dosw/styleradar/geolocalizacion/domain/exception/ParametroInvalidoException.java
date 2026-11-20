package edu.dosw.styleradar.geolocalizacion.domain.exception;

public class ParametroInvalidoException extends DomainException {

    public ParametroInvalidoException(String message) {
        super(message, "PARAMETRO_INVALIDO");
    }
}
