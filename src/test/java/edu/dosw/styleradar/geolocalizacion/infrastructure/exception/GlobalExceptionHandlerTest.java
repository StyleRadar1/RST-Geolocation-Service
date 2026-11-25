package edu.dosw.styleradar.geolocalizacion.infrastructure.exception;

import edu.dosw.styleradar.geolocalizacion.domain.exception.DomainException;
import edu.dosw.styleradar.geolocalizacion.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void notFoundReturns404() {
        // Arrange
        ResourceNotFoundException ex = new ResourceNotFoundException("No existe", "ALMACEN_NO_ENCONTRADO");

        // Act
        ProblemDetail detail = handler.handleNotFound(ex);

        // Assert
        assertThat(detail.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(detail.getDetail()).isEqualTo("No existe");
    }

    @Test
    void domainErrorReturns422() {
        // Arrange
        DomainException ex = new DomainException("Regla incumplida");

        // Act
        ProblemDetail detail = handler.handleDomain(ex);

        // Assert
        assertThat(detail.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        assertThat(detail.getTitle()).isEqualTo("Regla de negocio incumplida");
    }

    @Test
    void validationErrorReturns400WithFields() {
        // Arrange
        BeanPropertyBindingResult result = new BeanPropertyBindingResult(new Object(), "request");
        result.addError(new FieldError("request", "nombre", "no debe estar vacío"));
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(result);

        // Act
        ProblemDetail detail = handler.handleValidation(ex);

        // Assert
        assertThat(detail.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(detail.getProperties()).containsEntry("errors", Map.of("nombre", "no debe estar vacío"));
    }

    @Test
    void missingParameterReturns400() {
        // Arrange
        MissingServletRequestParameterException ex = new MissingServletRequestParameterException("lat", "double");

        // Act
        ProblemDetail detail = handler.handleBadParameter(ex);

        // Assert
        assertThat(detail.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void unexpectedErrorReturns500() {
        // Arrange
        Exception ex = new IllegalStateException("fallo");

        // Act
        ProblemDetail detail = handler.handleUnexpected(ex);

        // Assert
        assertThat(detail.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(detail.getDetail()).isEqualTo("Ocurrió un error inesperado");
    }

    @Test
    void unknownRouteReturns404() {
        NoResourceFoundException ex = new NoResourceFoundException(HttpMethod.GET, "/");
        ProblemDetail detail = handler.handleRouteNotFound(ex);
        assertThat(detail.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(detail.getTitle()).isEqualTo("Ruta no encontrada");
    }

    @Test
    void methodNotAllowedReturns405() {
        HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException("POST");
        ProblemDetail detail = handler.handleMethodNotAllowed(ex);
        assertThat(detail.getStatus()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED.value());
        assertThat(detail.getProperties()).containsEntry("code", "METODO_NO_PERMITIDO");
    }

    @Test
    void everyErrorHasItsCode() {
        assertThat(handler.handleNotFound(new ResourceNotFoundException("x", "ALMACEN_NO_ENCONTRADO")).getProperties()).containsEntry("code", "ALMACEN_NO_ENCONTRADO");
        assertThat(handler.handleDomain(new DomainException("x", "MOTIVO_REQUERIDO")).getProperties()).containsEntry("code", "MOTIVO_REQUERIDO");
        assertThat(handler.handleUnexpected(new RuntimeException("x")).getProperties()).containsEntry("code", "ERROR_INTERNO");
    }

    @Test
    void errorsIncludeTimestamp() {
        assertThat(handler.handleUnexpected(new RuntimeException("x")).getProperties()).containsKeys("code", "timestamp");
    }
}
