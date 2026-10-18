package cl.duoc.barriodigital.rabbitadmin.service;

import cl.duoc.barriodigital.rabbitadmin.config.RabbitMQNames;
import cl.duoc.barriodigital.rabbitadmin.dto.QueueInfo;
import cl.duoc.barriodigital.rabbitadmin.exception.ParametroInvalidoException;
import cl.duoc.barriodigital.rabbitadmin.exception.RecursoNoEncontradoException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

// Único punto del microservicio que llama a RabbitAdmin: los controllers no conocen la
// librería de RabbitMQ, solo invocan estos métodos y reciben DTOs o excepciones de dominio.
@Service
public class RabbitAdminService {

	private final RabbitAdmin rabbitAdmin;

	public RabbitAdminService(RabbitAdmin rabbitAdmin) {
		this.rabbitAdmin = rabbitAdmin;
	}

	public String crearCola(String nombre) {
		exigirNoVacio(nombre, "queueName");
		return rabbitAdmin.declareQueue(new Queue(nombre, true));
	}

	public void eliminarCola(String nombre) {
		exigirNoVacio(nombre, "queueName");
		boolean eliminada = rabbitAdmin.deleteQueue(nombre);
		if (!eliminada) {
			throw new RecursoNoEncontradoException("No existe la cola " + nombre);
		}
	}

	public int purgarCola(String nombre) {
		exigirNoVacio(nombre, "queueName");
		return rabbitAdmin.purgeQueue(nombre);
	}

	public QueueInfo consultarCola(String nombre) {
		exigirNoVacio(nombre, "queueName");
		Properties propiedades = rabbitAdmin.getQueueProperties(nombre);
		if (propiedades == null) {
			throw new RecursoNoEncontradoException("No existe la cola " + nombre);
		}
		// RabbitAdmin no entrega siempre el mismo tipo numérico aquí (Integer o Long según el caso).
		int mensajesPendientes = ((Number) propiedades.get(RabbitAdmin.QUEUE_MESSAGE_COUNT)).intValue();
		int consumidores = ((Number) propiedades.get(RabbitAdmin.QUEUE_CONSUMER_COUNT)).intValue();
		return new QueueInfo(nombre, mensajesPendientes, consumidores);
	}

	public void crearExchange(String nombre, String tipo) {
		exigirNoVacio(nombre, "exchangeName");
		exigirNoVacio(tipo, "type");
		rabbitAdmin.declareExchange(construirExchange(nombre, tipo));
	}

	public void eliminarExchange(String nombre) {
		exigirNoVacio(nombre, "exchangeName");
		boolean eliminado = rabbitAdmin.deleteExchange(nombre);
		if (!eliminado) {
			throw new RecursoNoEncontradoException("No existe el exchange " + nombre);
		}
	}

	public void crearBinding(String nombreCola, String nombreExchange, String routingKey) {
		exigirNoVacio(nombreCola, "queueName");
		exigirNoVacio(nombreExchange, "exchangeName");
		exigirNoVacio(routingKey, "routingKey");
		rabbitAdmin.declareBinding(construirBinding(nombreCola, nombreExchange, routingKey));
	}

	public void eliminarBinding(String nombreCola, String nombreExchange, String routingKey) {
		exigirNoVacio(nombreCola, "queueName");
		exigirNoVacio(nombreExchange, "exchangeName");
		exigirNoVacio(routingKey, "routingKey");
		rabbitAdmin.removeBinding(construirBinding(nombreCola, nombreExchange, routingKey));
	}

	private Binding construirBinding(String nombreCola, String nombreExchange, String routingKey) {
		return new Binding(nombreCola, Binding.DestinationType.QUEUE, nombreExchange, routingKey, null);
	}

	private Exchange construirExchange(String nombre, String tipo) {
		return switch (tipo.toLowerCase()) {
			case "direct" -> new DirectExchange(nombre);
			case "topic" -> new TopicExchange(nombre);
			case "fanout" -> new FanoutExchange(nombre);
			case "headers" -> new HeadersExchange(nombre);
			default -> throw new ParametroInvalidoException(
					"type debe ser direct, topic, fanout o headers (recibido: " + tipo + ")");
		};
	}

	// Métricas de las 3 colas funcionales y sus DLQ (sección "Métricas" de la guía RabbitMQ).
	// Se omite silenciosamente la cola que aún no exista en vez de fallar toda la respuesta.
	public List<QueueInfo> obtenerMetricas() {
		List<QueueInfo> metricas = new ArrayList<>();
		Stream.concat(RabbitMQNames.COLAS_PRINCIPALES.stream(), RabbitMQNames.COLAS_DLQ.stream())
				.forEach(nombre -> {
					Properties propiedades = rabbitAdmin.getQueueProperties(nombre);
					if (propiedades != null) {
						metricas.add(new QueueInfo(
								nombre,
								((Number) propiedades.get(RabbitAdmin.QUEUE_MESSAGE_COUNT)).intValue(),
								((Number) propiedades.get(RabbitAdmin.QUEUE_CONSUMER_COUNT)).intValue()));
					}
				});
		return metricas;
	}

	private void exigirNoVacio(String valor, String nombreParametro) {
		if (valor == null || valor.isBlank()) {
			throw new ParametroInvalidoException(nombreParametro + " es obligatorio");
		}
	}

}
