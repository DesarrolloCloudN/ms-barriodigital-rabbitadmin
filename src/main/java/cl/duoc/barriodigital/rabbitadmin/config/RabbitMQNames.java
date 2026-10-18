package cl.duoc.barriodigital.rabbitadmin.config;

import java.util.List;

// Nombres de la topología fija de BarrioDigital (debe coincidir con RabbitMQNames de
// ms-barriodigital-requests/notify). Este microservicio los usa solo para informar métricas;
// la creación/eliminación de recursos ad-hoc acepta cualquier nombre que pida quien llama la API.
public final class RabbitMQNames {

	private RabbitMQNames() {
	}

	public static final List<String> COLAS_PRINCIPALES = List.of(
			"q.cmd.email", "q.cmd.crew", "q.cmd.certificate");

	public static final List<String> COLAS_DLQ = List.of(
			"q.cmd.email.dlq", "q.cmd.crew.dlq", "q.cmd.certificate.dlq");

}
