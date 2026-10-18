package cl.duoc.barriodigital.rabbitadmin.exception;

// Parámetro de entrada vacío o inválido; el GlobalExceptionHandler responde HTTP 400.
public class ParametroInvalidoException extends RuntimeException {

	public ParametroInvalidoException(String message) {
		super(message);
	}

}
