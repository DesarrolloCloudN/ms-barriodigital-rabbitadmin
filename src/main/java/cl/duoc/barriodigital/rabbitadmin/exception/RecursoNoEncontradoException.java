package cl.duoc.barriodigital.rabbitadmin.exception;

// La cola/exchange consultado no existe en el broker; el GlobalExceptionHandler responde HTTP 404.
public class RecursoNoEncontradoException extends RuntimeException {

	public RecursoNoEncontradoException(String message) {
		super(message);
	}

}
