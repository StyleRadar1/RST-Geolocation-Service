package edu.dosw.styleradar.geolocalizacion.infrastructure.exception;

import edu.dosw.styleradar.geolocalizacion.domain.exception.DomainException;
import edu.dosw.styleradar.geolocalizacion.domain.exception.ParametroInvalidoException;
import edu.dosw.styleradar.geolocalizacion.domain.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        return problem(HttpStatus.NOT_FOUND, "RECURSO_NO_ENCONTRADO", "Recurso no encontrado", ex.getMessage());
    }

    @ExceptionHandler(ParametroInvalidoException.class)
    public ProblemDetail handleParametroInvalido(ParametroInvalidoException ex) {
        return problem(HttpStatus.BAD_REQUEST, "SOLICITUD_INVALIDA", "Solicitud inválida", ex.getMessage());
    }

    @ExceptionHandler(DomainException.class)
    public ProblemDetail handleDomain(DomainException ex) {
        return problem(HttpStatus.UNPROCESSABLE_ENTITY, "REGLA_DE_NEGOCIO", "Regla de negocio incumplida", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        ProblemDetail detail = problem(HttpStatus.BAD_REQUEST, "SOLICITUD_INVALIDA", "Solicitud inválida", "La solicitud tiene campos inválidos");
        detail.setProperty("errors", errors);
        return detail;
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class})
    public ProblemDetail handleBadParameter(Exception ex) {
        return problem(HttpStatus.BAD_REQUEST, "FORMATO_INVALIDO", "Solicitud inválida", "Falta un parámetro o tiene un formato inválido");
    }

    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    public ProblemDetail handleRouteNotFound(Exception ex) {
        return problem(HttpStatus.NOT_FOUND, "RUTA_NO_ENCONTRADA", "Ruta no encontrada", "El recurso o ruta solicitada no existe");
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex) {
        return problem(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR_INTERNO", "Error interno", "Ocurrió un error inesperado");
    }

    private ProblemDetail problem(HttpStatus status, String code, String title, String message) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(status, message);
        detail.setTitle(title);
        detail.setProperty("code", code);
        return detail;
    }
}
