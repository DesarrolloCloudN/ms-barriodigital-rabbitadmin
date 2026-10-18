package cl.duoc.barriodigital.rabbitadmin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

// Centraliza el manejo de errores de todos los controllers.
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ParametroInvalidoException.class)
	public ResponseEntity<Map<String, String>> handleParametroInvalido(ParametroInvalidoException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
	}

	@ExceptionHandler(RecursoNoEncontradoException.class)
	public ResponseEntity<Map<String, String>> handleRecursoNoEncontrado(RecursoNoEncontradoException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
	}

}
